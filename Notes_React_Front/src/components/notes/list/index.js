import React, { Fragment, useState} from 'react';
import { Button, Column, Tag, Title, List, Input, Label } from "rbx";
import Moment from 'moment';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faShare, faTrash } from '@fortawesome/free-solid-svg-icons'

function ListNotes(props) {
  const [email, setEmail] = useState("");
  return (
    <Fragment>
      <Column.Group breakpoint="mobile">
        <Column size={6} offset={1}>
          <Title size={6}>
            {props.notes.length} Notes
          </Title>
          </Column>
          <Column size={2}>
            <Button state="active" color="custom-purple" outlined size="small" onClick={() => props.createNote()}>
              Notes +
            </Button>
          </Column>
      </Column.Group>
      <List className="notes-list">
        {props.notes.map((item, key) =>
          <List.Item key={key} onClick={() => props.selectNote(item.id)} active={item == props.current_note}>
            <Title size={5}>
              {item.title.replace(/(<([^>]+)>)/ig, "").substring(0,15)}
            </Title>
            <Title size={6} subtitle spaced={false}>
              {item.body.replace(/(<([^>]+)>)/ig, "").substring(0,30)}
            </Title>

            <Column.Group breakpoint="mobile">
              <Column size={9}>
              <p>Criada em: {item.created_at}</p>
              <p>Atualizada em: {item.updated_at}</p>
              <hr/>
              </Column>
              <Column size={2}>
                <FontAwesomeIcon 
                  icon={faTrash} 
                  onClick={() => props.deleteNote(item)}
                  color="grey"
                />
              </Column>
            </Column.Group>
          </List.Item>
        )}
      </List>
    </Fragment>
  )
}

export default ListNotes;