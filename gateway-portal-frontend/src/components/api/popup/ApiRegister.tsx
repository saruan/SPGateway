import React, {useEffect, useState} from 'react';
import {Button, Col, Form, Row} from "react-bootstrap";
import {ModalComponent} from "../../common/ModalComponent";
import {getRequest} from "../../../perist/axios";

/**
 * API 상세 팝업용 데이터
 * @param data  API 상세 데이터
 * @constructor
 */
export function ApiRegister() {


  const [click, setClick] = useState<boolean>(false)
  const [serviceLoginType, setServiceLoginType] = useState([])
  const [serviceAuthType, setServiceAuthType] = useState([])

  const handleClose = () => {

    setClick(false);
  };

  /**
   * Popup Open EventListener
   */
  const handleOpen = () => {

    setClick(true);
    getRequest(serviceLoginTypeCodeCallback, '/portal/code/ServiceLoginType'
        , new URLSearchParams());
    getRequest(serviceAuthTypeCodeCallback, '/portal/code/ServiceAuthType'
        , new URLSearchParams());
  }

  /**
   * ServiceLoginType Code List Select Result Callback Function
   * @param data  EnumType
   */
  const serviceLoginTypeCodeCallback = (data) => {

    setServiceLoginType(data)
  }

  const serviceAuthTypeCodeCallback = (data) => {

    setServiceAuthType(data)
  }

  const RegisterForm = (
      <>
        <Form>
          <Form.Group as={Row} controlId="serviceNm">
            <Form.Label column>
              API명
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" placeholder="서비스 명"/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="servicePath">
            <Form.Label column>
              Gateway URL
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" placeholder="G/W URL"/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="serviceTargetUrl">
            <Form.Label column>
              Proxy Pass
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" placeholder="목적지 URL"/>
            </Col>
          </Form.Group>
          <Form.Group as={Row}>
            <Form.Label column>
              로그인 방법
            </Form.Label>
            <Col sm={10}>
              <Form.Control as="select" className="mr-sm-2" id="serviceLoginType" custom>
                {
                  serviceLoginType.map((data: any) => (
                      <option value={data.type}>{data.desc}</option>
                  ))
                }
              </Form.Control>
            </Col>
          </Form.Group>
          <Form.Group as={Row}>
            <Form.Label column>
              인증 방법
            </Form.Label>
            <Col sm={10}>
              <Form.Control as="select" className="mr-sm-2" id="serviceAuthType" custom>
                {
                  serviceAuthType.map((data: any) => (
                      <option value={data.type}>{data.desc}</option>
                  ))
                }
              </Form.Control>
            </Col>
          </Form.Group>
        </Form>
      </>
  );

  return (
      <>
        {ModalComponent(handleClose, click, "API 등록", RegisterForm)}
        <Button variant="outline-info" onClick={handleOpen}>
          등록
        </Button>
      </>
  );
}
