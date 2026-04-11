import axios from 'axios';

const BASE_URL = 'http://localhost:8083/courses';

export const getAllCourses = () => axios.get(BASE_URL);

export const searchCourses = (title) =>
    axios.get(`${BASE_URL}/search`, { params: { title } });

export const addCourse = (course) => axios.post(BASE_URL, course);

export const deleteCourse = (id) => axios.delete(`${BASE_URL}/${id}`);

export const sendChatMessage = (message) =>
    axios.post(`${BASE_URL}/chatbot`, { message });

