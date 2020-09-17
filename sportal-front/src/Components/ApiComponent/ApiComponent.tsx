import React from 'react';
import ApiListComponent from './SubComponent/ApiListComponent';
import ApiSearchComponent from "./SubComponent/ApiSearchComponent";

export default class ApiComponent extends React.Component {

  private apiList: any;

  render() {
    return (
        <div>
          <div className="api_main_type1">
            <ApiSearchComponent/>
          </div>
          <div style={{height: "80vh"}}>
            <ApiListComponent/>
          </div>
        </div>
    )
  }
}