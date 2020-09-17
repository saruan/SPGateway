import React from 'react';
import {BrowserRouter, Route} from "react-router-dom";
import ApiComponent from "../Components/ApiComponent/ApiComponent";
import AppList from "../Components/AppComponent/AppListComponent";

class ContentsLayer extends React.Component {
  render() {
    return (
        <div className="contents_main_type1">
          <BrowserRouter>
            <Route path='/menu/api' component={ApiComponent}/>
            <Route path='/menu/app' component={AppList}/>
          </BrowserRouter>
        </div>
    )
  }
}

export default ContentsLayer;