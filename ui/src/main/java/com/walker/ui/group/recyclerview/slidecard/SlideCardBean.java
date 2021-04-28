package com.walker.ui.group.recyclerview.slidecard;

import java.util.ArrayList;
import java.util.List;

public class SlideCardBean {
    private int postition;
    private String url;
    private String name;

    public SlideCardBean(int postition, String url, String name) {
        this.postition = postition;
        this.url = url;
        this.name = name;
    }

    public int getPostition() {
        return postition;
    }

    public SlideCardBean setPostition(int postition) {
        this.postition = postition;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public SlideCardBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getName() {
        return name;
    }

    public SlideCardBean setName(String name) {
        this.name = name;
        return this;
    }

    public static List<SlideCardBean> initDatas() {
        List<SlideCardBean> datas = new ArrayList<>();
        int i = 1;
        datas.add(new SlideCardBean(i++, "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171122%2Fedbb7db8b84d4d96b29dea3055e1e0bb.jpeg&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622187613&t=a946085df1557af8a66a678b8cf3bb0e", "黄山"));
        datas.add(new SlideCardBean(i++, "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp2-q.mafengwo.net%2Fs8%2FM00%2F8C%2FC7%2FwKgBpVWWxuuADgGxAADMmWsQL_M73.jpeg%3FimageMogr2%2Fthumbnail%2F%21440x260r%2Fgravity%2FCenter%2Fcrop%2F%21440x260%2Fquality%2F100&refer=http%3A%2F%2Fp2-q.mafengwo.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622187677&t=cdfb67f7d189ca7457a284f5e68c2611", "华山"));
        datas.add(new SlideCardBean(i++, "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171210%2F275f2be6a3704086934c5864f3ee7f5d.jpeg&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622187725&t=6fdeb028a30dd9a151c1264075a53851", "泰山"));
        datas.add(new SlideCardBean(i++, "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Farticle-fd.zol-img.com.cn%2Ft_s640x2000%2Fg5%2FM00%2F0A%2F06%2FChMkJ1pEj46ISoBvAAG7m_bB7IkAAjkbgHFpq4AAbuz819.jpg&refer=http%3A%2F%2Farticle-fd.zol-img.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622187805&t=eec1433a3f93a045817aa18bf49808f7", "漓江"));
        datas.add(new SlideCardBean(i++, "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.80tian.com%2Ftravel%2Fline%2F201402%2Fss14022815177494.jpg&refer=http%3A%2F%2Fimg.80tian.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622187859&t=ac1e318df75fa37fb32679e3c3056043", "张家界"));
        datas.add(new SlideCardBean(i++, "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180720%2Febd3f0bb96a14b56b5f782b05b5cee8f.jpg&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622187941&t=2e008cb2ee2d5f7f1613ab40e6f3efe7", "玉龙雪山"));
        datas.add(new SlideCardBean(i++, "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimage6.pinlue.com%2Fwechat%2Fimg_png%2Fhqbfnpsh6MptziaFcLdaKvWGlibKwtXCyZwcIGzqDibqj1bViav0cHxA8FOicyQna44SSmIK3cavcX5sJppQugXNsTA%2F0.png&refer=http%3A%2F%2Fimage6.pinlue.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622187987&t=9daef2b5017c5b8b9fad19b364b81188", "香格里拉"));
        datas.add(new SlideCardBean(i++, "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdpic.tiankong.com%2F31%2F0x%2FQJ6665748936.jpg%3Fx-oss-process%3Dstyle%2Fshow&refer=http%3A%2F%2Fdpic.tiankong.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622188057&t=a79c0afda66fc4fa8b415cb68606d19d", "布达拉宫"));
        datas.add(new SlideCardBean(i++, "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.news18a.com%2Fcommunity%2F20170514%2Fxc_59183eb4aaf47.jpg&refer=http%3A%2F%2Fimg.news18a.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622188116&t=ca922d9708848032e0f6d120f7dce24c", "西湖"));
        datas.add(new SlideCardBean(i++, "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnews.eastday.com%2Fimages%2Fthumbnailimg%2Fmonth_1610%2F201610010403098090.jpg&refer=http%3A%2F%2Fnews.eastday.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622188163&t=f009e64593b3aee9fcbc8ec40bf87709", "天安门"));

        return datas;
    }
}
