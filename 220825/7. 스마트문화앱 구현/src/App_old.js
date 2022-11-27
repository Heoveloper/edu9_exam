import React, { useState } from 'react';
import { Dimensions, StatusBar } from 'react-native';
import styled, { ThemeProvider } from 'styled-components/native';
import { theme } from './theme'
import Input from './components/Input';
import EventButton from './components/EventButton';
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

  const _saveTasks = async tasks => {
    try {
      await AsyncStorage.setItem('tasks', JSON.stringify(tasks));
      setTasks(tasks);
    } catch (e) {
      console.error(e);
    }
  };

  const _loadTasks = async () => {
    const loadedTasks = await AsyncStorage.getItem('tasks');
    setTasks(JSON.parse(loadedTasks || '{}'));
  };

  const _addTask = () => {
    const ID = Date.now().toString();
    const newTaskObject = {
      [ID]: { id: ID, text: newTask, completed: false },
    };
    setNewTask('');
    _saveTasks({...tasks, ...newTaskObject});
  };

  const _handleTextChange = text => {
    setNewTask(text);
  }

  const _deleteTask = id => {
    const currentTasks = Object.assign({}, tasks);
    delete currentTasks[id];
    _saveTasks(currentTasks);
  };

  const _toggleTask = id => {
    const currentTasks = Object.assign({}, tasks);
    currentTasks[id]['completed'] = !currentTasks[id]['completed'];
    _saveTasks(currentTasks);
  };

  const _updateTask = item => {
    const currentTasks = Object.assign({}, tasks);
    currentTasks[item.id] = item;
    _saveTasks(currentTasks);
  };

  const _onBlur = () => {
    setNewTask('');
  };

  const _deleteCompletedTask = item => {
    if (obj === 'null') return null;
    else {
      delete currentTasks[item.completed];
      _saveTasks(currentTasks);
    }
  }

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
        <EventButton deleteCompletedTask={_deleteCompletedTask}/>
      </Container>
    </ThemeProvider>
  ) : (
    <AppLoading   
      startAsync={()=>{_loadTasks('tasks')}}
      onFinish={() => setIsReady(true)}
      onError={console.error}
    />
  ); 
}