package com.example.ddspackage;
// CoreDX DDL Generated code.  Do not modify - modifications may be overwritten.
import com.toc.coredx.DDS.*;
import java.nio.*;


public final class dataDDSTypeSupport implements TypeSupport{
  
  public ReturnCode_t register_type(DomainParticipant dp, String type_name) {
    if (dp.check_version( "3", "4", "0_p9" ) != 0) {
      System.out.println( "WARNING: dataDDS TypeSupport version does not match CoreDX Library version.");
      System.out.println( "This may cause software instability or crashes.");
    }
    return dp.register_type(this, type_name);
  }

  public String get_type_name()   { return "dataDDS"; }
  public long   getCTypeSupport() { return cTypeSupport; }

  public dataDDSTypeSupport() {
    dataDDS tmp = new dataDDS();
    cTypeSupport = DomainParticipant.createTypeSupport(this, 
                     getClass().getName(), tmp.getClass().getName());
  }

  // ------------------------------------------------------
  // implementation

  public DataReader   create_datareader(Subscriber sub, TopicDescription td, 
                                        SWIGTYPE_p__DataReader jni_dr) {
    return new dataDDSDataReader(sub, td, jni_dr);
  }
  public DataWriter   create_datawriter(Publisher  pub, Topic topic, SWIGTYPE_p__DataWriter jni_dw ) {
    return new dataDDSDataWriter(pub, topic, jni_dw);
  }

  // marshal variants
  public int     marshall ( ByteBuffer out_stream,  dataDDS src ) {
    int size = 0;
    if ( out_stream == null ) {
      size += 4; // for CDR 'header' 
      // src.XVel_DDS
      size = (size+3) & 0xfffffffc;// align 4
      size += 4;
      // src.YVel_DDS
      size = (size+3) & 0xfffffffc;// align 4
      size += 4;
      // src.CompassDir_DDS
      size = (size+3) & 0xfffffffc;// align 4
      size += 4;
      // src.GPS_LN_DDS
      size = (size+3) & 0xfffffffc;// align 4
      size += 4;
      // src.GPS_LT_DDS
      size = (size+3) & 0xfffffffc;// align 4
      size += 4;
      // src.Log_DDS
      size = (size+3) & 0xfffffffc;// align 4
      size += 4; // length
      size += (src.Log_DDS==null)?1:(src.Log_DDS.length()+1);
      // src.data_image_DDS
      size += 1;
    } else {
      out_stream.clear();
      if ((1 & 0x01)==0) out_stream.order(ByteOrder.BIG_ENDIAN);
      else               out_stream.order(ByteOrder.LITTLE_ENDIAN);
      
      // add CDR 'header' 
      out_stream.put((byte)0x00);
      out_stream.put((byte)1);
      out_stream.put((byte)0x00); // flags
      out_stream.put((byte)0x00); // flags
      
      // src.XVel_DDS
      while((out_stream.position() & 0x03) != 0) out_stream.put((byte)0x00); // align 4
      out_stream.putFloat(src.XVel_DDS);
      // src.YVel_DDS
      while((out_stream.position() & 0x03) != 0) out_stream.put((byte)0x00); // align 4
      out_stream.putFloat(src.YVel_DDS);
      // src.CompassDir_DDS
      while((out_stream.position() & 0x03) != 0) out_stream.put((byte)0x00); // align 4
      out_stream.putFloat(src.CompassDir_DDS);
      // src.GPS_LN_DDS
      while((out_stream.position() & 0x03) != 0) out_stream.put((byte)0x00); // align 4
      out_stream.putFloat(src.GPS_LN_DDS);
      // src.GPS_LT_DDS
      while((out_stream.position() & 0x03) != 0) out_stream.put((byte)0x00); // align 4
      out_stream.putFloat(src.GPS_LT_DDS);
      // src.Log_DDS
      while((out_stream.position() & 0x03) != 0) out_stream.put((byte)0x00); // align 4
      out_stream.putInt(src.Log_DDS.length()+1);
      out_stream.put(src.Log_DDS.getBytes());
      out_stream.put((byte)0);
      // src.data_image_DDS
      out_stream.put((byte)src.data_image_DDS);
      size = out_stream.position();
    }
    return size;
  }

  public int     marshall_fixed_size (  ) { return 0; }
  public int     marshall_key ( ByteBuffer out_stream,  dataDDS src ) {
    int size = 0;
    if ( out_stream == null ) {
      size += 4; // for CDR 'header' 
    } else {
      out_stream.clear();
      if ((1 & 0x01)==0) out_stream.order(ByteOrder.BIG_ENDIAN);
      else               out_stream.order(ByteOrder.LITTLE_ENDIAN);
      
      // add CDR 'header' 
      out_stream.put((byte)0x00);
      out_stream.put((byte)1);
      out_stream.put((byte)0x00); // flags
      out_stream.put((byte)0x00); // flags
      
      size = out_stream.position();
    }
    return size;
  }

  public int     marshall_key_hash ( ByteBuffer out_stream,  dataDDS src ) {
    int size = 0;
    if ( out_stream == null ) {
    } else {
      out_stream.clear();
      out_stream.order(ByteOrder.BIG_ENDIAN);
      
      size = out_stream.position();
    }
    return size;
  }

  public boolean key_must_hash (  ) { return false; }

  // unmarshal variants
  public int     unmarshall ( dataDDS t, ByteBuffer data, int s )    {
    data.get();                      // skip the first byte 
    byte encoding = data.get();      // data encoding CDR / ENDIAN 
    data.getShort();                 // unused flags (2 bytes)
    if ((encoding & 0x01)==0)  data.order(ByteOrder.BIG_ENDIAN);
    else                       data.order(ByteOrder.LITTLE_ENDIAN);

    if ((encoding & 0x02)==0) { // CDR encoding
      // t.XVel_DDS
      while((data.position() & 0x03) != 0) data.position(data.position()+1); // align 4
      t.XVel_DDS = data.getFloat();
      // t.YVel_DDS
      while((data.position() & 0x03) != 0) data.position(data.position()+1); // align 4
      t.YVel_DDS = data.getFloat();
      // t.CompassDir_DDS
      while((data.position() & 0x03) != 0) data.position(data.position()+1); // align 4
      t.CompassDir_DDS = data.getFloat();
      // t.GPS_LN_DDS
      while((data.position() & 0x03) != 0) data.position(data.position()+1); // align 4
      t.GPS_LN_DDS = data.getFloat();
      // t.GPS_LT_DDS
      while((data.position() & 0x03) != 0) data.position(data.position()+1); // align 4
      t.GPS_LT_DDS = data.getFloat();
      // t.Log_DDS
      {
        while((data.position() & 0x03) != 0) data.position(data.position()+1); // align 4
        int    slen6;
        slen6   = data.getInt()-1;// skip trailing null
        byte[] sbytes6 = new byte[slen6];
        data.get(sbytes6,0,slen6);
        data.get(); // skip trailing null
        t.Log_DDS   = new String(sbytes6);
        sbytes6 = null;
      }
      // t.data_image_DDS
      t.data_image_DDS = data.get();
    }
    return 1; // 1==success, 0==failure
  }

  public int     unmarshall_key( dataDDS t, ByteBuffer data, int s ) {
    data.get();                      // skip the first byte 
    byte encoding = data.get();      // data encoding CDR / ENDIAN 
    data.getShort();                 // unused flags (2 bytes)
    if ((encoding & 0x01)==0)  data.order(ByteOrder.BIG_ENDIAN);
    else                       data.order(ByteOrder.LITTLE_ENDIAN);

    if ((encoding & 0x02)==0) { // CDR encoding
    }
    return 1; // 1==success, 0==failure
  }

  public int     unmarshall_key_hash( dataDDS t, ByteBuffer data, int s ) {
    data.order(ByteOrder.BIG_ENDIAN);
    return 0;
  }

  public int gen_typecode( ByteBuffer b ) {
    byte[] tc_data = { 
(byte)0x0a, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xe8, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x00, (byte)0x00, (byte)0x00, (byte)'d', (byte)'a', (byte)'t', (byte)'a', (byte)'D', (byte)'D', (byte)'S', (byte)0x00, (byte)0x07, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x1c, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x09, (byte)0x00, (byte)0x00, (byte)0x00, (byte)'X', (byte)'V', (byte)'e', (byte)'l', (byte)'_', (byte)'D', (byte)'D', (byte)'S', (byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x1a, (byte)0x00, (byte)0x09, (byte)0x00, (byte)0x00, (byte)0x00, (byte)'Y', (byte)'V', (byte)'e', (byte)'l', (byte)'_', (byte)'D', (byte)'D', (byte)'S', (byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x1e, (byte)0x00, (byte)0x0f, (byte)0x00, (byte)0x00, (byte)0x00, (byte)'C', (byte)'o', (byte)'m', (byte)'p', (byte)'a', (byte)'s', (byte)'s', (byte)'D', (byte)'i', (byte)'r', (byte)'_', (byte)'D', (byte)'D', (byte)'S', (byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff, (byte)0x00, (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x1a, (byte)0x00, (byte)0x0b, (byte)0x00, (byte)0x00, (byte)0x00, (byte)'G', (byte)'P', (byte)'S', (byte)'_', (byte)'L', (byte)'N', (byte)'_', (byte)'D', (byte)'D', (byte)'S', (byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff, (byte)0x00, (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x1a, (byte)0x00, (byte)0x0b, (byte)0x00, (byte)0x00, (byte)0x00, (byte)'G', (byte)'P', (byte)'S', (byte)'_', (byte)'L', (byte)'T', (byte)'_', (byte)'D', (byte)'D', (byte)'S', (byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff, (byte)0x00, (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x20, (byte)0x00, (byte)0x08, (byte)0x00, (byte)0x00, (byte)0x00, (byte)'L', (byte)'o', (byte)'g', (byte)'_', (byte)'D', (byte)'D', (byte)'S', (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0d, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x06, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x20, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0f, (byte)0x00, (byte)0x00, (byte)0x00, (byte)'d', (byte)'a', (byte)'t', (byte)'a', (byte)'_', (byte)'i', (byte)'m', (byte)'a', (byte)'g', (byte)'e', (byte)'_', (byte)'D', (byte)'D', (byte)'S', (byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff, (byte)0x00, (byte)0x00, (byte)0x09, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,     };
    if (b != null) b.put(tc_data, 0, tc_data.length);
    return tc_data.length;
  }

  public int get_typecode_enc( ) {
    return (1 & 0x01);
  }

  // key field operations
  public boolean has_key (  ) { return false; }

  // <type> operations
  public dataDDS         alloc ()              { return new dataDDS(); }
  public void       clear (dataDDS instance)   { instance.clear(); }
  public void       destroy (dataDDS instance) { /* noop */ }
  public void       copy (dataDDS to, dataDDS from) { to.copy(from); }
  public boolean    get_field( String fieldname, CoreDX_FieldDef fdef ) { 
    return false;
  }
  private long cTypeSupport = 0;
}; // dataDDS
