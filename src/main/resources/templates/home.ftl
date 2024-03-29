<#include "header.ftl">

    <div id="main">
        <div class="container" id="daily">
            <div class="jscroll-inner">
                <div class="daily">
                    <#assign cur_date = ''>
                    <#list vos as vo>
                    <#assign newsDate = vo.news.createdDate?string("yyyy-MM-dd")>

                    <#if (cur_date != newsDate)>

                        <#if (vo?index > 0)>
                            </div>
                        </#if>
                        <#assign cur_date = newsDate>

                    <h3 class="date">
                        <i class="fa icon-calendar"></i>
                        <span>头条资讯 &nbsp; ${vo.news.createdDate?string("yyyy-MM-dd")}</span>
                    </h3>

                    <div class="posts">
                    </#if>

                        <div class="post">
                            <div class="votebar">
                                <#if (vo.like > 0)>
                                <button class="click-like up pressed" data-id="${vo.news.id}" title="赞同"><i class="vote-arrow"></i><span class="count">${vo.news.likeCount}</span></button>
                                <#else>
                                <button class="click-like up" data-id="${vo.news.id}" title="赞同"><i class="vote-arrow"></i><span class="count">${vo.news.likeCount}</span></button>
                                </#if>
                                <#if (vo.like < 0)>
                                <button class="click-dislike down pressed" data-id="${vo.news.id}" title="反对"><i class="vote-arrow"></i></button>
                                <#else>
                                <button class="click-dislike down" data-id="${vo.news.id}" title="反对"><i class="vote-arrow"></i></button>
                                </#if>
                            </div>
                            <div class="content" data-url="/news/$!{news.id}">
                                <div >
                                    <img class="content-img" src="${vo.news.image}" alt="">
                                </div>
                                <div class="content-main">
                                    <h3 class="title">
                                        <a target="_blank" rel="external nofollow" href="/news/${vo.news.id}">${vo.news.title}</a>
                                    </h3>
                                    <div class="meta">
                                        ${vo.news.link}
                                        <span>
                                            <i class="fa icon-comment"></i> ${vo.news.commentCount}
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="user-info">
                                <div class="user-avatar">
                                    <a href="/user/${vo.user.id}/"><img width="32" class="img-circle" src="${vo.user.headUrl}"></a>
                                </div>

                                <!--
                                <div class="info">
                                    <h5>分享者</h5>

                                    <a href="http://nowcoder.com/u/251205"><img width="48" class="img-circle" src="http://images.nowcoder.com/images/20141231/622873_1420036789276_622873_1420036771761_%E8%98%91%E8%8F%87.jpg@0e_200w_200h_0c_1i_1o_90Q_1x" alt="Thumb"></a>

                                    <h4 class="m-b-xs">冰燕</h4>
                                    <a class="btn btn-default btn-xs" href="http://nowcoder.com/signin"><i class="fa icon-eye"></i> 关注TA</a>
                                </div>
                                -->
                            </div>

                            <div class="subject-name">来自 <a href="/user/${vo.user.id}/">${vo.user.name}</a></div>
                        </div>


                    <#if vo?counter == vos?size>
                    </div>
                    </#if>

                    </#list>


                </div>
            </div>
        </div>

    </div>



    <#if pop??>
        <script>
            window.loginpop = ${pop};


        </script>


    </#if>
<script type="text/javascript" src="/scripts/main/site/home.js"></script>



<#include "footer.ftl">
