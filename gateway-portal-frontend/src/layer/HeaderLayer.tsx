import React from 'react';
import {Nav, Navbar} from 'react-bootstrap'
import axios from 'axios';

export default class HeaderLayer extends React.Component {

  state = {
    menuList: []
  }

  componentDidMount() {

    this.getMenuList();
  }

  /**
   * Menu목록 조회
   */
  getMenuList() {

    axios.get('/portal/v1.0/menu').then(res => {

      const menus = res.data;

      this.setState({

        menuList: menus
      });
    })
  }

  render() {

    return (
        <div>
          <Navbar bg="dark" variant={"dark"} expand="lg">
            <Navbar.Brand href="/">SPortal</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav"/>
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="mr-auto">
                {this.state.menuList.map((menus, index) => {

                  let menu: any = menus;
                  return (<Nav.Link key={index} href={menu.menuUrl}>{menu.menuNm}</Nav.Link>)
                })}
              </Nav>
            </Navbar.Collapse>
          </Navbar>
        </div>
    )
  }
}