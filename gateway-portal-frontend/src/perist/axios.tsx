import axios from "axios";
import React from "react";

/**
 * 공통 Axios Get 요청
 * @param callback
 * @param url
 * @param params
 */
export function getRequest(callback: Function, url: string, params: URLSearchParams) {

  axios.get(url, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("access_token"),
    },
    params: params
  })
  .then(res => {

    callback(res.data.resultData);
  }).catch((error) => errorCallback(error));
}

/**
 * 공통 Axios Post 요청
 * @param callback
 * @param url
 * @param params
 */
export function postRequest(callback: Function, url: string, params: any) {

  axios.post(url, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("access_token"),
    },
    params: params
  })
  .then(res => {

    if (res.data.resultCode !== '200') {

      alert(res.data.resultMessage);
    }

    callback(res.data.resultData);
  }).catch((error) => {

    if (error.response) {

      alert(error.response.data.resultMessage)
    } else if (error.request) {

      alert(error.request)
    } else {

      alert(error.message)
    }
  });
}

/**
 * 공통 Axios Put 요청
 * @param callback
 * @param url
 * @param params
 */
export function putRequest(callback: Function, url: string, params: any) {

  axios.put(url,
      params, {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("access_token"),
        }
      })
  .then(res => {
    if (res.data.resultCode !== '200') {

      alert(res.data.resultMessage);
    }

    callback(res.data.resultData);
  }).catch((error) => {

    if (error.response) {

      alert(error.response.data.resultMessage)
    } else if (error.request) {

      alert(error.request)
    } else {

      alert(error.message)
    }
  });
}

/**
 * 공통 Axios Put 요청
 * @param callback
 * @param url
 * @param params
 */
export function deleteRequest(callback: Function, url: string) {

  axios.delete(url, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("access_token"),
    }
  })
  .then(res => {

    if (res.data.resultCode !== '200') {

      alert(res.data.resultMessage);
    }

    callback(res.data.resultData);
  }).catch((error) => {

    if (error.response) {

      alert(error.response.data.resultMessage)
    } else if (error.request) {

      alert(error.request)
    } else {

      alert(error.message)
    }
  });
}

/**
 * Axios 오류 처리용 Callback 함수
 * @param error error object
 */
const errorCallback = (error) => {

  if (error.response) {

    if (error.response.status === 401) {

      //window.location.href = "/"
    } else {

      alert(error.response.data.resultMessage)
    }

  } else {

    alert(error.message)
  }
}