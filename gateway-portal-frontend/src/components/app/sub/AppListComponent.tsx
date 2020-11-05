import React, {useEffect, useState} from 'react';
import {BootstrapTable, TableHeaderColumn} from "react-bootstrap-table"
import moment from "moment";
import {getRequest} from "../../../perist/axios";
import {AppDetails} from "../popup/AppDetails";
import {AppManage} from "../popup/AppManage";


/**
 * App 목록 조회용 컴포넌트
 * @constructor
 */
export function AppListComponent() {

  const [appList, setAppList] = useState([]);

  useEffect(() => {

    getRequest((data) => setAppList(data), '/portal/service/v1.0/app', new URLSearchParams())
  }, [])

  /**
   * App List 갱신
   */
  const refreshAppList = () => getRequest((data) => setAppList(data),
      '/portal/service/v1.0/app', new URLSearchParams())

  /**
   * Row 비고 버튼 정의
   * @param cell  Cell
   * @param row   Row
   */
  const detailPopup = (cell, row) => {

    return (
        <AppDetails refreshList={refreshAppList} appId={row.appId}/>
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
        <BootstrapTable data={appList}>
          <TableHeaderColumn dataField='appId' isKey hidden>APP ID</TableHeaderColumn>
          <TableHeaderColumn dataField='appNm' headerAlign="center">APP 명</TableHeaderColumn>
          <TableHeaderColumn dataField='appDesc' headerAlign="center"
                             dataAlign="center">APP 설명</TableHeaderColumn>
          <TableHeaderColumn dataField='regDt' dataFormat={dateFormatter}
                             headerAlign="center" dataAlign="center">등록일</TableHeaderColumn>
          <TableHeaderColumn dataField='serviceId' headerAlign="center"
                             dataAlign="center" dataFormat={detailPopup}>비고</TableHeaderColumn>
        </BootstrapTable>
        {<div className="button_right_type1">
          <AppManage isUpdate={false} appId={0} refreshList={refreshAppList}/>
        </div>}
      </div>
  )
}