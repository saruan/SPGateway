import React, {useEffect} from 'react';
import {BrowserRouter, Route} from "react-router-dom";
import ApiComponent from "../components/api/ApiComponent";
import AppComponent from "../components/app/AppComponent";
import axios from "axios";
import LoginForm from "../components/common/LoginForm";

export default function ContentsLayer() {

  // 임시 세션 처리
  useEffect(() => {

    const token = localStorage.getItem("access_token")

    if (window.location.pathname != '/') {

      if (token === null) {

        alert("로그인 필요")
        window.location.href = "/"
      }

      axios.get("/portal/v1.0/user/validate/token", {
        headers: {
          Authorization: "Bearer " + token,
        }
      }).catch((error) => {

        if (error.response.status === 401) {

          alert("세션 만료")
          window.location.href = "/"
        }
      });
    }
  }, [window.location.pathname]);

  return (
      <div className="outer">
        <BrowserRouter>
          <Route exact path="/" component={LoginForm}/>
          <Route path="/portal/api" component={ApiComponent}/>
          <Route path="/portal/app" component={AppComponent}/>
        </BrowserRouter>
      </div>
  )
}
