import React from 'react';
import './App.css'
import HeaderLayer from "./layer/HeaderLayer";
import ContentsLayer from "./layer/ContentsLayer";
import FooterLayer from "./layer/FooterLayer";

function App() {

  return (
    <div className="App">
      <HeaderLayer/>
      <ContentsLayer/>
      <FooterLayer/>
    </div>
  );
}

export default App;
