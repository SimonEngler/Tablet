package com.example.zoetablet;

import com.example.zoetablet.BasicFragmentActivity;
import com.toc.coredx.DDS.*;

public class Writer {
  
  public String shape;
  public String color;
  public int    size;
  public int    x;
  public int    y;
  public int    dx;
  public int    dy;
  public DynamicTypeDataWriter dw;

  public Writer(DynamicTypeDataWriter dw, String shape, String color, int size, int x, int y, int dx, int dy) {
    this.dw    = dw;
    this.shape = shape;
    this.color = color;
    this.size  = size;
    this.x     = x;
    this.y     = y;
    this.dx    = dx;
    this.dy    = dy;
  }

  public void updatePosition() {
    int w2 = 1 + this.size / 2;
    this.x = this.x + this.dx;
    this.y = this.y + this.dy;
    if (this.x < w2)
      {
	//delta = w2 -d this.x;
	this.x = w2;
	this.dx = -this.dx;
      }
    if (this.x > 240 - w2)
      {
	//delta = this.x - (240-w2);
	this.x = (240-w2);
	this.dx = -this.dx;
      }
    if (this.y < w2)
      {
	//delta = w2 - this.y;
	this.y = w2;
	this.dy = -this.dy;
      }
    if (this.y > 270-w2)
      {
	//delta = this.y - (270-w2);
	this.y = (270-w2);
	this.dy = -this.dy;
      }
  }

  public void publish() {
    // construct a 'data' sample 
    StringDynamicType color = (StringDynamicType)BasicFragmentActivity.shape_type.get_field(0); /* color */
    LongDynamicType x = (LongDynamicType)BasicFragmentActivity.shape_type.get_field(1);
    LongDynamicType y = (LongDynamicType)BasicFragmentActivity.shape_type.get_field(2);
    LongDynamicType sz = (LongDynamicType)BasicFragmentActivity.shape_type.get_field(3);
    color.set_string(this.color);
    x.set_long(this.x);
    y.set_long(this.y);
    sz.set_long(this.size);
    dw.write(BasicFragmentActivity.shape_type, null);
  }
};