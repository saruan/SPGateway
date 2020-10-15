import axios from "axios";

/**
 * 공통 Axios Get 요청
 * @param callback
 * @param url
 * @param params
 */
export function getRequest(callback: Function, url: string, params: URLSearchParams) {

  axios.get(url, {
    params : params
  })
  .then(res => {

    if(res.data.resultCode !== '200') {

      alert(res.data.resultMessage);
    }

    callback(res.data.resultData);
  }).catch((err) => {

    alert(err);
  });
}

/**
 * 공통 Axios Post 요청
 * @param callback
 * @param url
 * @param params
 */
export function postRequest(callback: Function, url: string, params: any) {

  axios.post(url, params)
  .then(res => {

    if(res.data.resultCode !== '200') {

      alert(res.data.resultMessage);
    }

    callback(res.data.resultData);
  }).catch((err) => {

    alert(err);
  });
}
