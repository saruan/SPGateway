import React, {useEffect} from 'react';
import {BrowserRouter, Route} from "react-router-dom";
import ApiComponent from "../components/api/ApiComponent";
import AppComponent from "../components/app/AppComponent";
import axios from "axios";
import LoginForm from "../components/common/LoginForm";
import RegisterForm from "../components/common/RegisterForm";

export default function ContentsLayer() {

  // 임시 세션 처리
  useEffect(() => {

    const token = localStorage.getItem("access_token")
    const path = window.location.pathname;
    const whitelist = ['/', '/portal/register'];

    // TODO 변경 필요
    if (!whitelist.includes(path)) {

      if (token === null) {

        alert("로그인 필요")
        window.location.href = "/"
      }

      axios.get("/gateway/oauth/state?token=" + token).catch((error) => {

        if (error.response.status === 400) {

          alert("세션 만료")
          window.location.href = "/"
        }
      });
    }
  }, []);

  return (
      <div className="outer">
        <BrowserRouter>
          <Route exact path="/" component={LoginForm}/>
          <Route path="/portal/api" component={ApiComponent}/>
          <Route path="/portal/app" component={AppComponent}/>
          <Route path="/portal/register" component={RegisterForm}/>
        </BrowserRouter>
      </div>
  )
}
