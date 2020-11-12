import React, {useEffect, useState} from 'react';
import {Nav, Navbar} from 'react-bootstrap'
import axios from 'axios';

export default function HeaderLayer() {

  const [menuList, setMenuList] = useState([])

  useEffect(() => {

    axios.get('/portal/v1.0/menu').then(res => setMenuList(res.data.resultData))
  }, []);

  return (
      <div>
        <Navbar bg="dark" variant={"dark"} expand="lg">
          <Navbar.Brand href="/">SPortal</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav"/>
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto">
              {menuList.map((menu: any, index) => {
                return (<Nav.Link key={index} href={menu.menuUrl}>{menu.menuNm}</Nav.Link>)
              })}
            </Nav>
          </Navbar.Collapse>
        </Navbar>
      </div>
  )
}