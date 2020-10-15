import React from 'react';
import {ApiListComponent} from './sub/ApiListComponent';
import ApiSearchComponent from "./sub/ApiSearchComponent";

export function ApiComponent() {

  return (
      <div>
        <div className="api_main_type1">
          <ApiSearchComponent/>
        </div>
        <ApiListComponent/>
      </div>
  )
}