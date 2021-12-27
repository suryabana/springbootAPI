package com.spring.response;

import com.spring.model.FileDB;

public class ResponseMessage {
  private String message;
  private FileDB file;

 

public FileDB getFile() {
	return file;
}

public void setFile(FileDB file) {
	this.file = file;
}

public ResponseMessage(String message, FileDB file) {
    this.message = message;
    this.file= file;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
