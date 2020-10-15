import React, {useState} from 'react';
import {ModalComponent} from "../../common/ModalComponent";
import {Form, Col, Row, Button} from "react-bootstrap";


/**
 * API 상세 팝업용 데이터
 * @param data  API 상세 데이터
 * @constructor
 */
export function ApiDetails(data) {

  let apiDetails = data.details;

  const DetailForm = (
      <>
        <Form>
          <Form.Group as={Row} controlId="serviceNm">
            <Form.Label column>
              API명
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={apiDetails.serviceNm}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="servicePath">
            <Form.Label column>
              G/W URL
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={apiDetails.servicePath}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="serviceTargetUrl">
            <Form.Label column>
              목적지 URL
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={apiDetails.serviceTargetUrl}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="filterBean">
            <Form.Label column>
              필터명
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={apiDetails.filterBean}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="serviceLoginType">
            <Form.Label column>
              로그인 타입
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={apiDetails.serviceLoginType}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="serviceAuthType">
            <Form.Label column>
              인증 타입
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={apiDetails.serviceAuthType}/>
            </Col>
          </Form.Group>
        </Form>
      </>
  );

  const [click, setClick] = useState<boolean>(false)
  const handleClose = () => {

    setClick(false);
  };

  const handleOpen = () =>  {

    setClick(true);
  }

  return (
      <>
        {ModalComponent(handleClose, click, "API 상세", DetailForm)}
        <Button variant="warning" onClick = {handleOpen}>
          상세
        </Button>
      </>
  );
}
