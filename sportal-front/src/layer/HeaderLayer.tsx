import React from 'react';
import {Nav, Navbar} from 'react-bootstrap'
import axios from 'axios';

export default class HeaderLayer extends React.Component {

  state = {
    menuList: []
  }

  componentDidMount() {

    this.getApiList();
  }

  getApiList() {

    axios.get('/sportal/menu').then(res => {
      const menus = res.data.resultData;

      this.setState({
        menuList: menus
      });
    })
  }

  render() {

    return (
        <div style={{height: "10vh"}}>
          <Navbar bg="dark" variant={"dark"} expand="lg">
            <Navbar.Brand href="/">SPortal</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav"/>
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="mr-auto">
                {this.state.menuList.map((menus, index) => {

                  let menu: any = menus;
                  return (<Nav.Link key={index} href={menu.menuUri}>{menu.menuNm}</Nav.Link>)
                })}
              </Nav>
            </Navbar.Collapse>
          </Navbar>
        </div>
    )
  }
}