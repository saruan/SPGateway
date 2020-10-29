import React, {useEffect, useState} from 'react';
import {Button, Modal} from "react-bootstrap";

interface ModelInterface {
  handleClose: any,
  isClicked: boolean,
  titleNm: String,
  bodyComponent: JSX.Element,
  isSave: boolean,
  saveEvent: any
}

/**
 * Common Modal Component
 * @param handleClose Popup Close Event
 * @param clicked is Event Called
 * @param titleNm Title Name
 * @param bodyComponent Body Component Code
 * @constructor
 */
export function ModalComponent({
                                 handleClose, isClicked, titleNm, bodyComponent,
                                 isSave, saveEvent
                               }: ModelInterface) {

  const [show, setShow] = useState(false);

  useEffect(() => {

    setShow(isClicked);

  }, [isClicked]);

  return (
      <>
        <Modal show={show} onHide={handleClose} animation={false} size={"lg"}>
          <Modal.Header closeButton>
            <Modal.Title>{titleNm}</Modal.Title>
          </Modal.Header>
          <Modal.Body>{bodyComponent}</Modal.Body>
          <Modal.Footer>
            {
              isSave ?
                  <Button variant="secondary" onClick={saveEvent}>
                    Save
                  </Button>
                  : <></>
            }
            <Button variant="secondary" onClick={handleClose}>
              Close
            </Button>
          </Modal.Footer>
        </Modal>
      </>
  );
}