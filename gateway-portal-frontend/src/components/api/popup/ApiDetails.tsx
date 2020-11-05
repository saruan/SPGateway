import React, {useState} from 'react';
import {ModalComponent} from "../../common/ModalComponent";
import {Button, Col, Form, Row} from "react-bootstrap";
import {ApiManage} from "./ApiManage";
import {deleteRequest} from "../../../perist/axios";

interface ApiDetailsInterface {
  data: any,
  serviceLoginType: any,
  serviceAuthType: any,
  filter: any,
  refreshList: Function
}

/**
 * API 상세 팝업용 데이터
 * @param data  API 상세 데이터
 * @param serviceLoginType  로그인 타입 코드 정보
 * @param serviceAuthType 인증 타입 코드 정보
 * @param filter
 * @constructor
 */
export function ApiDetails({
                             data, serviceLoginType, serviceAuthType,
                             filter, refreshList
                           }: ApiDetailsInterface) {

  const [click, setClick] = useState<boolean>(false)

  /**
   * 팝업 Close Event
   */
  const handleClose = () => {

    setClick(false);
  };

  /**
   * 팝업 Open Event
   */
  const handleOpen = () => {

    setClick(true);
  }

  /**
   * API 삭제 호출
   */
  const deleteApi = () => {

    deleteRequest(() => refreshList(), '/api/service/v1.0.0/routes/' + data.serviceId);
  }

  const DetailForm = (
      <>
        <Form>
          <Form.Group as={Row} controlId="serviceNm">
            <Form.Label column>
              API명
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={data.serviceNm}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="servicePath">
            <Form.Label column>
              G/W URL
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={data.servicePath}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="serviceTargetUrl">
            <Form.Label column>
              목적지 URL
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={data.serviceTargetUrl}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="filterBean">
            <Form.Label column>
              필터명
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={data.filterBean}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="serviceLoginType">
            <Form.Label column>
              로그인 타입
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={data.serviceLoginType}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="serviceAuthType">
            <Form.Label column>
              인증 타입
            </Form.Label>
            <Col sm={10}>
              <Form.Control type="text" readOnly value={data.serviceAuthType}/>
            </Col>
          </Form.Group>
        </Form>
      </>
  );

  return (
      <>
        <ModalComponent handleClose={handleClose} isClicked={click} titleNm="API 상세"
                        bodyComponent={DetailForm} isSave={false} saveEvent={handleClose}/>
        <Button variant="outline-secondary" onClick={handleOpen}>
          상세
        </Button>{' '}
        <ApiManage isUpdate={true} serviceDetail={data} filter={filter}
                   serviceLoginType={serviceLoginType} serviceAuthType={serviceAuthType}
                   refreshList={refreshList}/>{' '}
        <Button variant="outline-secondary" onClick={deleteApi}>
          삭제
        </Button>{' '}
      </>
  );
}
