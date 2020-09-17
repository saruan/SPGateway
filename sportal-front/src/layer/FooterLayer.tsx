import React from 'react';
import {Navbar} from 'react-bootstrap'

class FooterLayer extends React.Component {
  render() {
    return (
        <div style={{height: "10vh"}}>
          <Navbar bg="light" fixed={"bottom"}>
            <Navbar.Brand>footer</Navbar.Brand>
          </Navbar>
        </div>
    )
  }
}

export default FooterLayer;