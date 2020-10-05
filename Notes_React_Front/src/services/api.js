const axios = require('axios').default;

const Api = axios.create({baseURL:'http://127.0.0.1:8080'});
export default Api;