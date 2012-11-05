package com.example.dds_subscriber;
// CoreDX DDL Generated code.  Do not modify - modifications may be overwritten.
import com.toc.coredx.DDS.*;


public final class dataDDSDataReader extends DataReader  {
  
  dataDDSDataReader (Subscriber             s,
                        TopicDescription       td,
                        SWIGTYPE_p__DataReader j_dr) {
     super(s,td,j_dr);
  }
  public ReturnCode_t    read( dataDDSSeq                   received_data,
                        SampleInfoSeq           sample_infos,
			 int                     max_samples,
			 long                    sample_states,
			 long                    view_states,
			 long                    instance_states) {
    sample_infos.value  = null;
    SampleSeq tmp_seq   = new SampleSeq();
    ReturnCode_t retval =  _read(tmp_seq, sample_infos, max_samples,
                                 sample_states, view_states, instance_states,
                                 dataDDS.class);
    received_data.value = (dataDDS[])tmp_seq.value;
    return retval;
  }
  public ReturnCode_t    read_w_condition( dataDDSSeq       received_data,
                        SampleInfoSeq           sample_infos,
			 int                     max_samples,
			 ReadCondition           condition) {
    sample_infos.value  = null;
    SampleSeq tmp_seq   = new SampleSeq();
    ReturnCode_t retval =  _read_w_condition(tmp_seq, sample_infos, max_samples,
                                 condition, dataDDS.class);
    received_data.value = (dataDDS[])tmp_seq.value;
    return retval;
  }
  public ReturnCode_t    read_next_sample( dataDDS          received_data,
                                    SampleInfo  sample_info) {
    ReturnCode_t retval;
    dataDDSSeq tmp_seq = new dataDDSSeq();
    SampleInfoSeq si_seq = new SampleInfoSeq();
    retval = read(tmp_seq, si_seq, 1, coredx.DDS_NOT_READ_SAMPLE_STATE, 
                             coredx.DDS_ANY_VIEW_STATE,
                             coredx.DDS_ANY_INSTANCE_STATE);
    if (retval == ReturnCode_t.RETCODE_OK) {
      received_data.copy(tmp_seq.value[0]);
      sample_info.copy(si_seq.value[0]);
      retval = ReturnCode_t.RETCODE_OK;
      return_loan(tmp_seq, si_seq);
    }
    return retval;
  }
  public ReturnCode_t    read_instance( dataDDSSeq     received_data,
                        SampleInfoSeq           sample_infos,
			 int                     max_samples,
			 InstanceHandle_t        handle,
			 long                    sample_states,
			 long                    view_states,
			 long                    instance_states) {
    sample_infos.value  = null;
    SampleSeq tmp_seq   = new SampleSeq();
    ReturnCode_t retval =  _read_instance(tmp_seq, sample_infos, max_samples,
                                 handle, sample_states, view_states, instance_states,
                                 dataDDS.class);
    received_data.value = (dataDDS[])tmp_seq.value;
    return retval;
  }
  public ReturnCode_t    read_next_instance( dataDDSSeq     received_data,
                        SampleInfoSeq           sample_infos,
			 int                     max_samples,
			 InstanceHandle_t        prev_handle,
			 long                    sample_states,
			 long                    view_states,
			 long                    instance_states) {
    sample_infos.value  = null;
    SampleSeq tmp_seq   = new SampleSeq();
    ReturnCode_t retval =  _read_next_instance(tmp_seq, sample_infos, max_samples,
                                 prev_handle, sample_states, view_states, instance_states,
                                 dataDDS.class);
    received_data.value = (dataDDS[])tmp_seq.value;
    return retval;
  }
  public ReturnCode_t    read_next_instance_w_condition( dataDDSSeq     received_data,
                        SampleInfoSeq           sample_infos,
			 int                     max_samples,
			 InstanceHandle_t        handle,
			 ReadCondition           condition) {
    sample_infos.value  = null;
    SampleSeq tmp_seq   = new SampleSeq();
    ReturnCode_t retval =  _read_next_instance_w_condition(tmp_seq, sample_infos, max_samples,
                                 handle, condition, dataDDS.class);
    received_data.value = (dataDDS[])tmp_seq.value;
    return retval;
  }
  public ReturnCode_t    take( dataDDSSeq                   received_data,
                        SampleInfoSeq           sample_infos,
			 int                     max_samples,
			 long                    sample_states,
			 long                    view_states,
			 long                    instance_states) {
    sample_infos.value  = null;
    SampleSeq tmp_seq = new SampleSeq();
    ReturnCode_t retval =  _take(tmp_seq, sample_infos, max_samples,
                                 sample_states, view_states, instance_states,
                                 dataDDS.class);
    received_data.value = (dataDDS[])tmp_seq.value;
    return retval;
  }
  public ReturnCode_t    take_w_condition( dataDDSSeq       received_data,
                        SampleInfoSeq           sample_infos,
			 int                     max_samples,
			 ReadCondition           condition) {
    sample_infos.value  = null;
    SampleSeq tmp_seq   = new SampleSeq();
    ReturnCode_t retval =  _take_w_condition(tmp_seq, sample_infos, max_samples,
                                 condition, dataDDS.class);
    received_data.value = (dataDDS[])tmp_seq.value;
    return retval;
  }
  public ReturnCode_t    take_next_sample( dataDDS          received_data,
                                    SampleInfo  sample_info) {
    ReturnCode_t retval;
    dataDDSSeq tmp_seq = new dataDDSSeq();
    SampleInfoSeq si_seq = new SampleInfoSeq();
    retval = take(tmp_seq, si_seq, 1, coredx.DDS_NOT_READ_SAMPLE_STATE, 
                             coredx.DDS_ANY_VIEW_STATE,
                             coredx.DDS_ANY_INSTANCE_STATE);
    if (retval == ReturnCode_t.RETCODE_OK) {
      received_data.copy(tmp_seq.value[0]);
      sample_info.copy(si_seq.value[0]);
      retval = ReturnCode_t.RETCODE_OK;
      return_loan(tmp_seq, si_seq);
    }
    return retval;
  }
  public ReturnCode_t    take_instance( dataDDSSeq     received_data,
                        SampleInfoSeq           sample_infos,
			 int                     max_samples,
			 InstanceHandle_t        handle,
			 long                    sample_states,
			 long                    view_states,
			 long                    instance_states) {
    sample_infos.value  = null;
    SampleSeq tmp_seq   = new SampleSeq();
    ReturnCode_t retval =  _take_instance(tmp_seq, sample_infos, max_samples,
                                 handle, sample_states, view_states, instance_states,
                                 dataDDS.class);
    received_data.value = (dataDDS[])tmp_seq.value;
    return retval;
  }
  public ReturnCode_t    take_next_instance( dataDDSSeq     received_data,
                        SampleInfoSeq           sample_infos,
			 int                     max_samples,
			 InstanceHandle_t        prev_handle,
			 long                    sample_states,
			 long                    view_states,
			 long                    instance_states) {
    sample_infos.value  = null;
    SampleSeq tmp_seq   = new SampleSeq();
    ReturnCode_t retval =  _take_next_instance(tmp_seq, sample_infos, max_samples,
                                 prev_handle, sample_states, view_states, instance_states,
                                 dataDDS.class);
    received_data.value = (dataDDS[])tmp_seq.value;
    return retval;
  }
  public ReturnCode_t    take_next_instance_w_condition( dataDDSSeq     received_data,
                        SampleInfoSeq           sample_infos,
			 int                     max_samples,
			 InstanceHandle_t        handle,
			 ReadCondition           condition) {
    sample_infos.value  = null;
    SampleSeq tmp_seq   = new SampleSeq();
    ReturnCode_t retval =  _take_next_instance_w_condition(tmp_seq, sample_infos, max_samples,
                                 handle, condition, dataDDS.class);
    received_data.value = (dataDDS[])tmp_seq.value;
    return retval;
  }
  public ReturnCode_t    return_loan( dataDDSSeq                   received_data,
                                      SampleInfoSeq           sample_infos) {
    ReturnCode_t retval =  _return_loan(null, sample_infos);
    return retval;
  }
  public ReturnCode_t     get_key_value(dataDDS                  key_holder,
                                        InstanceHandle_t    handle) {
    ReturnCode_t retval  = _get_key_value(key_holder, handle);
    return retval;
  }
  public InstanceHandle_t lookup_instance(dataDDS                  instance_data) {
    InstanceHandle_t retval = _lookup_instance(instance_data);
    return retval;
  }
}; // dataDDS
