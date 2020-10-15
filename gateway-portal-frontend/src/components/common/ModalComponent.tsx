import React, {useEffect, useState} from 'react';
import {Button, Modal} from "react-bootstrap";

export function ModalComponent(handleClose: any, clicked: boolean, titleNm: String, bodyComponent: JSX.Element) {

  const [show, setShow] = useState(false);

  useEffect(() =>{

    setShow(clicked);

  }, [clicked]);

  return (
      <>
        <Modal show={show} onHide={handleClose} animation={false} size={"lg"}>
          <Modal.Header closeButton>
            <Modal.Title>{titleNm}</Modal.Title>
          </Modal.Header>
          <Modal.Body>{bodyComponent}</Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Close
            </Button>
          </Modal.Footer>
        </Modal>
      </>
  );
}