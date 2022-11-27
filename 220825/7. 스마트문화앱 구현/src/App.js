import React, { useRef, useState } from 'react';
import { Dimensions, StatusBar, Alert } from 'react-native';
import styled, { ThemeProvider } from 'styled-components/native';
import { theme } from './theme'
import Input from './components/Input';
// import EventButton from './components/EventButton';
import LineButton from './components/LineButton';
import Task from './components/Task'
import AsyncStorage from '@react-native-async-storage/async-storage';
// import { AppLoading } from 'expo'; 
import AppLoading from 'expo-app-loading';


const Container = styled.View`
  flex: 1;
  background-color: ${({theme}) => theme.background};
  align-items: center;
  justify-content: flex-start;
`;

const Title = styled.Text`
  /* width: ${({width}) => width - 40}px; */
  width: 80%;
  font-size: 40px;
  font-weight: 600;
  color: ${({theme}) => theme.text};
  background-color: ${({theme}) => theme.itemBackground};
  align-self: center;
  text-align: center;
  margin: 0px 20px;
`;

const List = styled.ScrollView`
  flex: 1;
  width: ${({width}) => width - 40}px;
`;

export default function App() {
  const width = Dimensions.get('window').width;

  const [isReady, setIsReady] = useState(false);
  const [newTask, setNewTask] = useState('');
  const [tasks, setTasks] = useState({});
  const refInput = useRef();

  const storeData = async (key, value) => {
    try {
      const jsonValue = JSON.stringify(value)
      await AsyncStorage.setItem(key, jsonValue)
      setTasks(value);
    } catch (e) {
      // saving error
    }
  }

  //로컬저장소에 데이터 가져오기
  const getData = async (key) => {
    try {
      const jsonValue = await AsyncStorage.getItem(key)
      const tasks = jsonValue != null ? JSON.parse(jsonValue) : {};
      setTasks(tasks);
    } catch(e) {
      // error reading value
    }
  }

  //로컬저장소 삭제 by key
  const removeValue = async (key) => {
    try {
      await AsyncStorage.removeItem(key);
    } catch(e) {
      // remove error
    }
  }

  //전체 삭제
  const clearAll = async () => {
    try {
      await AsyncStorage.clear();
    } catch(e) {
      // clear error
    }
  }

  const _addTask = () => {
    const ID = Date.now().toString();
    const newTaskObject = {
      [ID]: { id: ID, text: newTask, completed: false },
    };
    setNewTask('');
    storeData('tasks', {...tasks, ...newTaskObject});
    // refInput?.current.focus();
  };

  const _handleTextChange = text => {
    setNewTask(text);
  }

  const _deleteTask = id => {
    const currentTasks = Object.assign({}, tasks);
    delete currentTasks[id];
    storeData('tasks', currentTasks);
  };

  const _toggleTask = id => {
    const currentTasks = Object.assign({}, tasks);
    currentTasks[id]['completed'] = !currentTasks[id]['completed'];
    storeData('tasks', currentTasks);
  };

  const _updateTask = item => {
    const currentTasks = Object.assign({}, tasks);
    currentTasks[item.id] = item;
    storeData('tasks', currentTasks);
  };

  const _onBlur = () => {
    setNewTask('');
  };

  //완료항목 전체 삭제
  const _delAllTask = () => {
    
    const currentTasks = {...tasks};

    //완료항목
    const completedTasks =
      Object.entries(currentTasks)
            .filter (task => task[1].completed==true);


    //완료항목이 없는 경우 확인창을 띄우지 않는다
    if (completedTasks.length < 0) return;

    //미완료항목
    const deleteCompletedItems = () => {
      const filteredTasks =
       Object.fromEntries(Object.entries(currentTasks)
                                .filter(task => task[1].completed == false));
      storeData('tasks', filteredTasks);
    }

    Alert.alert(
      "삭제",                             //경고창 제목
      "완료항목 전체를 삭제하시겠습니까?",  //경고창 메세지
      [
        {
          text: "예",
          onPress: () => deleteCompletedItems(),
        },
        { text: "아니오",
          onPress: () => {}
        }
      ]
    );
  };

  return isReady ? (
    <ThemeProvider theme={theme}>
      <Container>
        <StatusBar
          barStyle="light-content"
          backgroundColor={theme.background}
        />
        <Title>버킷 리스트</Title>
        <Input
          placeholder="+항목추가"
          value={newTask}
          onChangeText={_handleTextChange}
          onSubmitEditing={_addTask}
          onBlur={_onBlur}
          ref={useRef}
        />
        <List width={width}>
          {Object.values(tasks)
                 .reverse()
                 .map(item => (
                  <Task
                    key={item.id}
                    item={item}
                    deleteTask={_deleteTask}
                    toggleTask={_toggleTask}
                    updateTask={_updateTask}
                  />
          ))}
        </List>
        <LineButton text="완료항목 전체삭제" onPressOut={_delAllTask}/>
      </Container>
    </ThemeProvider>
  ) : (
    <AppLoading   
      //앱 로딩 전 실행할 로직    
      startAsync={()=>{getData('tasks')}}
      //startAsync호출이 성공적으로 수행되면
      onFinish={() => setIsReady(true)}
      //startAsync호출이 실패하면
      onError={console.warn}
    />
  ); 
}