import React, {useState} from 'react';
import {ModalComponent} from "../../common/ModalComponent";
import {Button, Col, Form, Row} from "react-bootstrap";
import {getRequest} from "../../../perist/axios";
import Table from "react-bootstrap/Table";
import {AppManage} from "./AppManage";

interface AppDetailsInterface {
  appId: number,
  refreshList: Function
}

/**
 * API 상세 팝업용 데이터
 * @param appId  API 상세 데이터
 * @param refreshList Refresh Function
 * @constructor
 */
export function AppDetails({appId, refreshList}: AppDetailsInterface) {

  const [click, setClick] = useState<boolean>(false)
  const [detail, setDetail] = useState<any>({
    appId: '',
    appNm: '',
    appKey: '',
    appDesc: '',
    serviceId: []
  });

  /**
   * 팝업 Close Event
   */
  const handleClose = () => setClick(false)

  /**
   * 팝업 Open Event
   */
  const handleOpen = () => {

    setClick(true)

    getRequest((data) => setDetail(data),
        '/gateway/portal/app/' + appId, new URLSearchParams())
  }

  /**
   * API 삭제 호출
   */
  /*
    const deleteApp = () => {

      deleteRequest(() => refreshList(), '/api/service/v1.0/routes/' + data.serviceId);
    }
  */

  const DetailForm = (
      <>
        <Form>
          <Form.Group as={Row} controlId="appNm">
            <Form.Label column>
              APP 이름
            </Form.Label>
            <Col sm={10}>
              <Form.Control readOnly value={detail.appNm}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="appKey">
            <Form.Label column>
              사용 APP KEY
            </Form.Label>
            <Col sm={10}>
              <Form.Control readOnly value={detail.appKey}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="appDesc">
            <Form.Label column>
              앱 설명
            </Form.Label>
            <Col sm={10}>
              <Form.Control readOnly value={detail.appDesc}/>
            </Col>
          </Form.Group>
          <Form.Group as={Row} controlId="serviceList">
            <Form.Label column>
              API 목록
            </Form.Label>
            <Col sm={10}>
              <Table striped bordered hover size="sm">
                <thead>
                <tr>
                  <th>API 명</th>
                  <th>인증 타입</th>
                  <th>필터</th>
                </tr>
                </thead>
                <tbody>
                {
                  detail.serviceId.map((data, index) => (
                          <tr key={index}>
                            <td>{data.serviceNm}</td>
                            <td>{data.serviceLoginType}</td>
                            <td>{data.filterBean}</td>
                          </tr>
                      )
                  )
                }
                </tbody>
              </Table>
            </Col>
          </Form.Group>
        </Form>
      </>
  );

  return (
      <>
        <ModalComponent handleClose={handleClose} isClicked={click} titleNm="APP 상세"
                        bodyComponent={DetailForm} isSave={false} saveEvent={handleClose}/>
        <Button variant="outline-secondary" onClick={handleOpen}>
          상세
        </Button>{' '}
        <AppManage isUpdate={true} appId={appId} refreshList={refreshList}/>{' '}
      </>
  );
}
