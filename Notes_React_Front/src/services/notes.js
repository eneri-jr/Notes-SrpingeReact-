import Api from "./api";

const NotesService = {
  
  index: () => Api.get("/notes", {
    headers: {'x-acess-token': localStorage.getItem('token')}
  }),
  
  create: () => Api.post('/notes', {'title': 'Nova nota', 'body': 'Nova nota...'}, {
    headers: {'x-acess-token': localStorage.getItem('token')}
  }),

  delete: (id) => Api.delete(`/notes/${id}`, {
    headers: {'x-acess-token': localStorage.getItem('token')}
    
  }),

  update: (id, params) => Api.put(`/notes/${id}`, params, {
    headers: {'x-acess-token': localStorage.getItem('token')}
  }),

  search: (query) => Api.get(`/notes/${query}`, {
    headers: {'x-acess-token': localStorage.getItem('token')}
  }),

  compart: (note, email) => Api.put('/notes/', {note,email}, {
    headers: {'x-acess-token': localStorage.getItem('token')}
  }),
}

export default NotesService;