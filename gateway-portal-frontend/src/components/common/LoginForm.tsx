import React, {useEffect, useState} from "react";
import axios from "axios";
import qs from "querystring";
import {Link} from "react-router-dom";

/**
 * Login 화면 Component
 * @constructor
 */
export default function LoginForm() {

  const loginStatus = localStorage.getItem("isLogin");
  const [isLogin, setIsLogin] = useState<boolean>(false);

  useEffect(() => {

    setIsLogin(loginStatus === 'true')
  }, [loginStatus])

  // Service Rest API 규격
  const [inputs, setInputs] = useState({
    id: '',
    password: ''
  })

  // QueryParam 생성
  const data = qs.stringify({
    id: inputs.id,
    password: inputs.password
  })

  // 로그인 프로세스 수행
  const login = () => {

    if(inputs.id === '' || inputs.password === ''){

      alert("ID 또는 비밀번호를 입력해 주세요.")
      return
    }

    axios.post("/portal/v1.0/user/login", data, {
      headers: {'content-type': 'application/x-www-form-urlencoded'},
    })
    .then(res => {

      // Session 정보 저장
      setIsLogin(true)
      localStorage.setItem("isLogin", "true");
      localStorage.setItem("access_token", res.data.accessToken);
      localStorage.setItem("userId", res.data.userNm);
    }).catch(ex => {

      const data = ex.response.data

      if (data.resultCode === 'USR001') {

        alert(data.resultMessage)
      }
    });
  }

  const logout = () => {

    localStorage.removeItem("access_token")
    localStorage.removeItem("isLogin")
    setIsLogin(false)
  }

  /**
   * Form Input onChange Event
   * @param e target
   */
  const onChange = (e) => {
    const {value, name} = e.target

    setInputs({
      ...inputs,
      [name]: value
    })
  }

  return (
      !isLogin ?
          <div className="inner">
            <form>
              <h3>Log in</h3>
              <div className="form-group">
                <label>아이디</label>
                <input type="text" className="form-control" placeholder="Enter ID"
                       name="id" value={inputs.id} onChange={onChange}/>
              </div>
              <div className="form-group">
                <label>패스워드</label>
                <input type="password" className="form-control" placeholder="Enter password"
                       name="password" value={inputs.password} onChange={onChange}/>
              </div>
              <button type="button" onClick={login} className="btn btn-dark btn-lg btn-block">로그인
              </button>
              <p className="forgot-password text-right">
                <Link to="/portal/register" className="btn btn-lg">회원가입</Link>
              </p>
            </form>
          </div>
          :
          <div className="inner">
            <button type="button" onClick={logout} className="btn btn-dark btn-lg btn-block">로그아웃
            </button>
          </div>
  )
}