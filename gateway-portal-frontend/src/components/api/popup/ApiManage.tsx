import React, {useState} from 'react';
import {Button, Col, Form, Row} from "react-bootstrap";
import {ModalComponent} from "../../common/ModalComponent";
import {postRequest, putRequest} from "../../../perist/axios";

interface ApiManageInterface {
  isUpdate: boolean,
  serviceDetail: any
  serviceLoginType: any,
  serviceAuthType: any,
  filter: any,
  refreshList: Function
}

/**
 * API 상세 팝업용 데이터
 * @constructor
 */
export function ApiManage({
                            isUpdate, serviceDetail, serviceLoginType,
                            serviceAuthType, filter, refreshList
                          }: ApiManageInterface) {

  const [click, setClick] = useState<boolean>(false)

  // Service Rest API 규격
  const [inputs, setInputs] = useState({
    serviceNm: '',
    servicePath: '',
    serviceTargetUrl: '',
    serviceDesc: '',
    serviceLoginType: '',
    serviceAuthType: '',
    filterId: '',
    regUserNo: 1,
    uptUserNo: 1
  })

  /**
   *   PopUp Close Event
   */
  const handleClose = () => {

    setClick(false)
    setInputs({
      serviceNm: '',
      servicePath: '',
      serviceTargetUrl: '',
      serviceDesc: '',
      serviceLoginType: '',
      serviceAuthType: '',
      filterId: '',
      regUserNo: 1,
      uptUserNo: 1
    })
  }

  /**
   *   Popup Open EventListener
   */
  const handleOpen = () => {

    setClick(true);

    if (isUpdate) {

      setInputs({
        serviceNm: serviceDetail.serviceNm,
        servicePath: serviceDetail.servicePath,
        serviceTargetUrl: serviceDetail.serviceTargetUrl,
        serviceDesc: serviceDetail.serviceDesc,
        serviceLoginType: serviceDetail.serviceLoginType,
        serviceAuthType: serviceDetail.serviceAuthType,
        filterId: serviceDetail.filterId,
        regUserNo: 1,
        uptUserNo: 1
      })
    }
  }

  /**
   * Register New API
   */
  const registerService = () => {

    if (inputs.serviceNm.trim() === '') {

      alert('서비스 명을 입력해 주세요.')
      return
    }

    if (inputs.servicePath.trim() === '') {

      alert('G/W 호출 URL을 입력해 주세요.')
      return
    }

    if (inputs.serviceTargetUrl.trim() === '') {

      alert('목적지 URL을 입력해 주세요.')
      return
    }

    postRequest(() => {

      handleClose();
      refreshList()
    }, '/api/service/v1/routes/', inputs)
  }

  /**
   * Update Api Service
   */
  const updateService = () => {

    if (inputs.serviceNm.trim() === '') {

      alert('서비스 명을 입력해 주세요.')
      return
    }

    if (inputs.servicePath.trim() === '') {

      alert('G/W 호출 URL을 입력해 주세요.')
      return
    }

    if (inputs.serviceTargetUrl.trim() === '') {

      alert('목적지 URL을 입력해 주세요.')
      return
    }

    putRequest(() => {

      handleClose()
      refreshList()
    }, '/api/service/v1/routes/' + serviceDetail.serviceId, inputs)
  }

  /**
   * Form Input onChange Event
   * @param e target
   */
  const onChange = (e) => {
    const {value, name} = e.target
    setInputs({
      ...inputs,
      [name]: value
    })
  }

  const ApiManageForm = (

      <Form>
        <Form.Group as={Row}>
          <Form.Label column>
            API명
          </Form.Label>
          <Col sm={10}>
            <Form.Control type="text" placeholder="서비스 명" name="serviceNm"
                          value={inputs.serviceNm} onChange={onChange}/>
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Form.Label column>
            Gateway URL
          </Form.Label>
          <Col sm={10}>
            <Form.Control type="text" placeholder="G/W URL" name="servicePath"
                          value={inputs.servicePath} onChange={onChange}/>
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Form.Label column>
            Proxy Pass
          </Form.Label>
          <Col sm={10}>
            <Form.Control type="text" placeholder="목적지 URL" name="serviceTargetUrl"
                          value={inputs.serviceTargetUrl} onChange={onChange}/>
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Form.Label column>
            서비스 설명
          </Form.Label>
          <Col sm={10}>
            <Form.Control type="text" placeholder="서비스 설명" name="serviceDesc"
                          value={inputs.serviceDesc} onChange={onChange}/>
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Form.Label column>
            로그인 방법
          </Form.Label>
          <Col sm={10}>
            <Form.Control as="select" className="mr-sm-2" name="serviceLoginType"
                          value={inputs.serviceLoginType} onChange={onChange}>
              <option hidden>선택</option>
              {
                serviceLoginType.map((data: any, index) => (
                    <option value={data.code} key={index}>{data.type}</option>
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
            <Form.Control as="select" className="mr-sm-2" name="serviceAuthType"
                          value={inputs.serviceAuthType} onChange={onChange}>
              <option hidden>선택</option>
              {
                serviceAuthType.map((data: any, index) => (
                    <option value={data.code} key={index}>{data.type}</option>
                ))
              }
            </Form.Control>
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Form.Label column>
            API 템플릿
          </Form.Label>
          <Col sm={10}>
            <Form.Control as="select" className="mr-sm-2" name="filterId"
                          value={inputs.filterId} onChange={onChange}>
              <option hidden>선택</option>
              {
                filter.map((data: any, index) => (
                    <option value={data.filterId} key={index}>{data.filterBean}</option>
                ))
              }
            </Form.Control>
          </Col>
        </Form.Group>
      </Form>
  );

  return (
      <>
        {
          isUpdate ?
              <>
                <ModalComponent handleClose={handleClose} isClicked={click} titleNm="API 수정"
                                bodyComponent={ApiManageForm} isSave={true}
                                saveEvent={updateService}/>
                <Button variant="outline-secondary" onClick={handleOpen}>
                  수정
                </Button>
              </> :
              <>
                <ModalComponent handleClose={handleClose} isClicked={click} titleNm="API 등록"
                                bodyComponent={ApiManageForm} isSave={true}
                                saveEvent={registerService}/>
                <Button variant="outline-secondary" onClick={handleOpen}>
                  등록
                </Button>
              </>
        }
      </>
  );
}
