package com.dramadownloader.app.api.v1;

import static spark.Spark.*;

public class HelloSpark {
  public static void main(String[] args) {
    get("/", (request, response) -> "Hello World!");
  }
}
