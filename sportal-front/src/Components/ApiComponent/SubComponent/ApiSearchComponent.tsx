import React from "react";
import {Button, FormControl, InputGroup} from "react-bootstrap";

export default class ApiSearchComponent extends React.Component {

  render() {
    return (
        <div>
          <InputGroup className="mb-3">
            <FormControl placeholder="API 검색" aria-describedby="basic-addon1"/>
            <InputGroup.Prepend>
              <Button variant="outline-secondary">검색</Button>
            </InputGroup.Prepend>
          </InputGroup>
        </div>
    )
  }
}