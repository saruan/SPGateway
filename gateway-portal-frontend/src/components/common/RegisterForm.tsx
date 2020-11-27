import React, {useState} from "react";
import qs from "querystring";
import axios from "axios";
import {postRequest} from "../../perist/axios";

/**
 * 회원 가입 템플릿
 * @constructor
 */
export default function RegisterForm() {

  // Service Rest API 규격
  const [inputs, setInputs] = useState({
    userLoginId: '',
    password: '',
    username: ''
  })

  const register = () => {

    if (inputs.userLoginId === '' || inputs.password === '' || inputs.username === '') {

      alert("값을 입력해 주세요.")
      return
    }

    axios.post("/portal/v1.0/user", inputs)
    .then(() => {

      alert("회원가입 성공")
      window.location.href = "/"
    }).catch(ex => {

      const data = ex.response.data

      if (data.resultCode === 'USR001') {

        alert(data.resultMessage)
      }
    });
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
      <div className="inner">
        <form>
          <div className="form-group">
            <label htmlFor="exampleInputEmail1">이름</label>
            <input type="text" className="form-control" id="username"
                   name="username" onChange={onChange} placeholder="Enter Name"/>
          </div>
          <div className="form-group">
            <label htmlFor="exampleInputEmail1">아이디</label>
            <input type="text" className="form-control" id="userLoginId"
                   name="userLoginId" onChange={onChange} placeholder="Enter ID"/>
          </div>
          <div className="form-group">
            <label htmlFor="exampleInputPassword1">비밀번호</label>
            <input type="password" className="form-control" id="password"
                   name="password" onChange={onChange} placeholder="Password"/>
          </div>
          <button type="button" onClick={register} className="btn btn-dark">회원가입</button>
        </form>
      </div>
  )
}