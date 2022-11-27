import React from 'react';
import { TouchableOpacity, Text } from 'react-native';
import PropTypes from 'prop-types';



const EventButton = ({onPress}) => {
    const _onPress = ({deleteCompletedTask}) => deleteCompletedTask;

    return (
        <TouchableOpacity
            style={{
                backgroundColor: '#101010',
            }}
            onPress={_onPress}
        >
            <Text style={{color: '#cfcfcf', fontSize: 40, letterSpacing: 5,}}>완료항목 전체삭제</Text>
        </TouchableOpacity>
    )
}

EventButton.propTypes = {
    deleteCompletedTask: PropTypes.func.isRequired,
};

export default EventButton;