import axios from "axios";

/**
 * 공통 Axios Get 요청
 * @param callback
 * @param url
 * @param params
 */
export function getRequest(callback: Function, url: string, params: URLSearchParams) {

  axios.get(url, {
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
 * 공통 Axios Post 요청
 * @param callback
 * @param url
 * @param params
 */
export function postRequest(callback: Function, url: string, params: any) {

  axios.post(url, params)

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

  axios.put(url, params)

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

  axios.delete(url)

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
