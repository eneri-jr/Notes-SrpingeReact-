import React, { Fragment, useEffect, useState } from 'react';
import { Column, Button, Section } from "rbx";
import "../../styles/notes.scss";
import { push as Menu } from 'react-burger-menu';
import List from "../notes/list";
import NoteService from '../../services/notes';
import Editor from "../notes/editor";
import Search from "../notes/search";


function Notes(props) {

  const [notes, setNotes] = useState([]);
  const [current_note, setCurrentNote] = useState({ title: "", body: "", id: "" });

  async function fetchNotes() {
    const response = await NoteService.index();
    if (response.data.length >= 1) {
      setNotes(response.data.reverse())
      setCurrentNote(response.data[0])
    }
  }

  const selectNote = (id) => {
    const note = notes.find((note) => {
      return note.id == id;
    })
    setCurrentNote(note);
  }
  
  useEffect(() => {
    fetchNotes();
  }, []);

  const createNote = async (params) => {
    const note = await NoteService.create();
    fetchNotes();
  }

  const deleteNote = async (note) => {
    await NoteService.delete(note.id);
    fetchNotes();
  }

  const updateNote = async (oldNote, params) => {
    console.log(params);
    await NoteService.update(oldNote.id, params);
    fetchNotes();
  }

  const searchNotes = async (query) => {
    const response = await NoteService.search(query);
    if (response.data.length >= 1) {
      setNotes(response.data.reverse())
      setCurrentNote(response.data[0])
    }
  }

  return (
    <Fragment>
      <Column.Group className="notes" id="notes">
        <Menu 
          pageWrapId={"notes-editor"} 
          isOpen={ props.isOpen }
          onStateChange={(state) => props.setIsOpen(state.isOpen)}
          disableAutoFocus 
          outerContainerId={"notes"}
          customBurgerIcon={ false }
          customCrossIcon={ false }
        >
          <Column.Group>
            <Column size={10} offset={1}>
              <Search searchNotes={searchNotes} fetchNotes={fetchNotes} />
            </Column>
          </Column.Group>
          <List
            notes={notes}
            selectNote={selectNote}
            deleteNote={deleteNote}
            createNote={createNote}
            current_note={current_note} />
        </Menu>


        <Column size={12} className="notes-editor" id="notes-editor">
          <Editor
            note={current_note}
            updateNote={updateNote}
          />
        </Column>
      </Column.Group>
    </Fragment>
  )
}

export default Notes;