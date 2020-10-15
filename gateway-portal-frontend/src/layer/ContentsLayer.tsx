import React from 'react';
import {BrowserRouter, Route} from "react-router-dom";
import {ApiComponent} from "../components/api/ApiComponent";
import AppList from "../components/app/AppListComponent";

export default function ContentsLayer(){
    return (
        <div className="contents_main_type1">
          <BrowserRouter>
            <Route path='/portal/api' component={ApiComponent}/>
            <Route path='/portal/app' component={AppList}/>
          </BrowserRouter>
        </div>
    )
}