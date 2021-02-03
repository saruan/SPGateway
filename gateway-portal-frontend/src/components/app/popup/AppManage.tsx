import React, {useState} from 'react';
import {Button, Col, Form, Row} from "react-bootstrap";
import {ModalComponent} from "../../common/ModalComponent";
import {getRequest, postRequest, putRequest} from "../../../perist/axios";

/**
 * Default Params Interface
 */
interface AppManageInterface {
  isUpdate: boolean,
  appId: number,
  refreshList: Function
}

/**
 * Input Type Interface
 */
interface AppInput {
  appNm: string,
  appDesc: string,
  serviceId: number[],
  regUserNo: string,
  uptUserNo: string
}

/**
 * Api List Interface
 */
interface ApiListInterface {
  serviceId: string,
  serviceNm: string,
  checked: boolean
}

/**
 * API 상세 팝업용 데이터
 * @constructor
 */
export function AppManage({isUpdate, appId, refreshList}: AppManageInterface) {

  const currentUserId = localStorage.getItem("userId")!

  const [click, setClick] = useState<boolean>(false)
  const [inputs, setInputs] = useState<AppInput>({
    appNm: '',
    appDesc: '',
    serviceId: [],
    regUserNo: currentUserId,
    uptUserNo: "1"
  })
  const [apiList, setApiList] = useState<ApiListInterface[]>([])

  /**
   *   PopUp Close Event
   */
  const handleClose = () => {

    setClick(false)
    setInputs({
      appNm: "",
      appDesc: "",
      serviceId: [],
      regUserNo: "",
      uptUserNo: ""
    })
  }

  /**
   *   Popup Open EventListener
   */
  const handleOpen = () => {

    setClick(true)
    initDetails()
  }

  /**
   * Initialize Forms
   * ApiList Call -> (Callback) Details Call -> (Callback) Checkbox, Input Init
   */
  const initDetails = () =>{

    getRequest((apis) => {

      if (isUpdate) {
        getRequest((data) => {

          const serviceIdList = data.serviceId.map(e => e.serviceId)

          // InputBox Init
          setInputs({
            appNm: data.appNm,
            appDesc: data.appDesc,
            serviceId: data.serviceId.map(e => e.serviceId),
            regUserNo: data.regUserNo,
            uptUserNo: currentUserId
          })

          // CheckBox Init
          apis.map(e => serviceIdList.indexOf(e.serviceId) >= 0 ?
              e.checked = true : e.checked = false)
          setApiList(apis)
        }, '/gateway/portal/app/' + appId, new URLSearchParams())
      }else{

        setApiList(apis)
      }
    }, '/gateway/portal/routes', new URLSearchParams())
  }

  /**
   * Register New App
   */
  const registerApp = () => {

    if (inputs.appNm.trim() === '') {

      alert('앱 명을 입력해 주세요.')
      return
    }

    postRequest(() => {
      handleClose();
      refreshList()
    }, '/gateway/portal/app', inputs)
  }

  /**
   * Update App Service
   */
  const updateApp = () =>
      putRequest(() => {
        handleClose()
        refreshList()
      }, '/gateway/portal/app/' + appId, inputs)


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

  /**
   * Checkbox onChange Event
   * @param e target
   */
  const onCheckboxChange = (e) => {

    const id = Number(e.target.id)
    const flag = e.target.checked

    if (flag) {

      inputs.serviceId.push(id)
    } else {

      const idx = inputs.serviceId.indexOf(id)
      inputs.serviceId.splice(idx)
    }
  }

  const AppManageForm = (

      <Form>
        <Form.Group as={Row}>
          <Form.Label column>
            APP 이름
          </Form.Label>
          <Col sm={10}>
            <Form.Control type="text" placeholder="앱 이름" name="appNm"
                          value={inputs.appNm} onChange={onChange}/>
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Form.Label column>
            앱 설명
          </Form.Label>
          <Col sm={10}>
            <Form.Control type="text" placeholder="앱 설명" name="appDesc"
                          value={inputs.appDesc} onChange={onChange}/>
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Form.Label column>
            API 선택
          </Form.Label>
          <Col sm={10}>
            {
              apiList.map((api, index) => {
                    return (
                        <Form.Check key={index} onChange={onCheckboxChange}
                                    label={api.serviceNm}
                                    id={api.serviceId}
                                    defaultChecked={api.checked}/>
                    )
                  })
            }
          </Col>
        </Form.Group>
      </Form>
  );

  return (
      <>
        {
          isUpdate ?
              <>
                <ModalComponent handleClose={handleClose} isClicked={click} titleNm="APP 수정"
                                bodyComponent={AppManageForm} isSave={true}
                                saveEvent={updateApp}/>
                <Button variant="outline-secondary" onClick={handleOpen}>
                  수정
                </Button>
              </> :
              <>
                <ModalComponent handleClose={handleClose} isClicked={click} titleNm="APP 등록"
                                bodyComponent={AppManageForm} isSave={true}
                                saveEvent={registerApp}/>
                <Button variant="outline-secondary" onClick={handleOpen}>
                  등록
                </Button>
              </>
        }
      </>
  );
}
