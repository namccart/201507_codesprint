#include <jni.h>
#include <vector>
#include <string>

// for gnuradio
#include <gnuradio/top_block.h>
#include <gnuradio/blocks/null_source.h>
#include <gnuradio/blocks/throttle.h>
#include <gnuradio/blocks/null_sink.h>

JavaVM *vm;
gr::top_block_sptr tb;
gr::blocks::null_source::sptr src;
gr::blocks::throttle::sptr thr;
gr::blocks::null_sink::sptr snk;

extern "C" {

JNIEXPORT void JNICALL
Java_org_gnuradio_grtemplate_GrTemplate_FgInit(JNIEnv* env,
                                               jobject thiz)
{
  GR_INFO("fg", "FgInit Called");

  tb = gr::make_top_block("fg");
  src = gr::blocks::null_source::make(sizeof(gr_complex));
  thr = gr::blocks::throttle::make(sizeof(gr_complex), 10000);
  snk = gr::blocks::null_sink::make(sizeof(gr_complex));

  tb->connect(src, 0, thr, 0);
  tb->connect(thr, 0, snk, 0);
}

JNIEXPORT void JNICALL
Java_org_gnuradio_grtemplate_GrTemplate_FgStart(JNIEnv* env,
                                                jobject thiz)
{
  GR_INFO("fg", "FgStart Called");
  tb->start();
}

JNIEXPORT void JNICALL
Java_org_gnuradio_grtemplate_GrTemplate_FgStop(JNIEnv* env,
                                               jobject thiz)
{
  GR_INFO("fg", "FgStop Called");
  tb->stop();
  tb->wait();
  GR_INFO("fg", "FgStop Exited");
}

}
