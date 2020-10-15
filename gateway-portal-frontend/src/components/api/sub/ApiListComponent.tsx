import React, {useEffect, useState} from 'react';
import {BootstrapTable, TableHeaderColumn} from "react-bootstrap-table"
import {ApiDetails} from "../popup/ApiDetails";
import moment from "moment";
import {ApiRegister} from "../popup/ApiRegister";
import {getRequest} from "../../../perist/axios";

/**
 * 상세 팝업 호출
 * @param cell  cell key value
 * @param row   row json data
 */
function detailPopup(cell, row) {

  return (
      <ApiDetails details={row}/>
  );
}

/**
 * 날짜 타입 변환
 * @param regDt
 */
function dateFormatter(regDt: any) {

  return `${moment(regDt).format("YYYY-MM-DD hh:mm")}`;
}

/**
 * Api 목록 조회용 컴포넌트
 * @constructor
 */
export function ApiListComponent() {

  /**
   * Api List Search
   */
  const [apiList, setApiList] = useState([]);

  useEffect(() => {

    getRequest(callback, '/api/service/v1/routes', new URLSearchParams());

  }, [])

  function callback(data) {

    setApiList(data);
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
          <TableHeaderColumn dataField='useYn' headerAlign="center"
                             dataAlign="center">사용 유무</TableHeaderColumn>
          <TableHeaderColumn dataField='serviceId' headerAlign="center"
                             dataAlign="center" dataFormat={detailPopup}>비고</TableHeaderColumn>
        </BootstrapTable>
        <div className="button_right_type1">
          <ApiRegister/>
        </div>
      </div>
  )
}