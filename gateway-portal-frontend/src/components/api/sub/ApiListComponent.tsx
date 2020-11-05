import React, {useEffect, useState} from 'react';
import {BootstrapTable, TableHeaderColumn} from "react-bootstrap-table"
import {ApiDetails} from "../popup/ApiDetails";
import moment from "moment";
import {ApiManage} from "../popup/ApiManage";
import {getRequest} from "../../../perist/axios";


/**
 * Api 목록 조회용 컴포넌트
 * @constructor
 */
export function ApiListComponent() {

  /**
   * Api List Search
   */
  const [apiList, setApiList] = useState([]);
  const [serviceLoginType, setServiceLoginType] = useState([])
  const [serviceAuthType, setServiceAuthType] = useState([])
  const [filter, setFilter] = useState([])

  useEffect(() => {

    getRequest((data) => setApiList(data), '/portal/service/v1.0/routes', new URLSearchParams());
    getRequest((data) => setServiceLoginType(data), '/portal/v1.0/code/ServiceLoginType'
        , new URLSearchParams());
    getRequest((data) => setServiceAuthType(data), '/portal/v1.0/code/ServiceAuthType'
        , new URLSearchParams());
    getRequest((data) => setFilter(data), '/portal/service/v1.0/filter'
        , new URLSearchParams());
  }, [])

  /**
   * API List 갱신
   */
  const refreshApiList = () => getRequest((data) => setApiList(data),
      '/portal/service/v1.0/routes', new URLSearchParams())

  /**
   * Row 비고 버튼 정의
   * @param cell  Cell
   * @param row   Row
   */
  const detailPopup = (cell, row) => {

    return (
        <ApiDetails data={row} filter={filter}
                    serviceLoginType={serviceLoginType} serviceAuthType={serviceAuthType}
                    refreshList={refreshApiList}/>
    );
  }

  /**
   * 날짜 타입 변환
   * @param regDt 등록일
   */
  const dateFormatter = (regDt: any) => {

    return `${moment(regDt).format("YYYY-MM-DD hh:mm")}`
  }

  return (
      <div>
        <BootstrapTable data={apiList}>
          <TableHeaderColumn dataField='serviceId' isKey hidden>API ID</TableHeaderColumn>
          <TableHeaderColumn dataField='serviceNm' headerAlign="center">API 명</TableHeaderColumn>
          <TableHeaderColumn dataField='servicePath' headerAlign="center"
                             dataAlign="center">G/W URL</TableHeaderColumn>
          <TableHeaderColumn dataField='regDt' dataFormat={dateFormatter}
                             headerAlign="center" dataAlign="center">등록일</TableHeaderColumn>
          <TableHeaderColumn dataField='serviceId' headerAlign="center"
                             dataAlign="center" dataFormat={detailPopup}>비고</TableHeaderColumn>
          <TableHeaderColumn dataField='serviceDesc' headerAlign="center" hidden/>
        </BootstrapTable>
        <div className="button_right_type1">
          <ApiManage isUpdate={false} serviceDetail={{}} filter={filter}
                     serviceLoginType={serviceLoginType} serviceAuthType={serviceAuthType}
                     refreshList={refreshApiList}/>
        </div>
      </div>
  )
}