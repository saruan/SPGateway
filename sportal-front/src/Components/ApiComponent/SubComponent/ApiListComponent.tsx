import React from 'react';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table'

export default class ApiListComponent extends React.Component {

  private apiList: any;

  render() {
    return (
        <div>
          <BootstrapTable data={this.apiList}>
            <TableHeaderColumn dataField='apiId' isKey>API ID</TableHeaderColumn>
            <TableHeaderColumn dataField='apiNm'>API 명</TableHeaderColumn>
            <TableHeaderColumn dataField='proxyUrl'>G/W URL</TableHeaderColumn>
            <TableHeaderColumn dataField='updateDt'>수정일</TableHeaderColumn>
            <TableHeaderColumn dataField='apiStatus'>상태</TableHeaderColumn>
          </BootstrapTable>
        </div>
    )
  }
}