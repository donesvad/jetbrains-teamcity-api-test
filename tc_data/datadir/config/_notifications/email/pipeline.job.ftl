<#import "pipeline.icons.ftl" as icons>
<#import "pipeline.section.ftl" as section>
<#import "pipeline.typography.ftl" as typography>


<#macro jobTitle serverUrl jobUrl status="failure" name=null time=null isSuccessful=false>
  <#if name?has_content>
    <table align="center" width="100%" id="job-name" style="margin:0;padding:0;line-height:14px" role="presentation" cellSpacing="0" cellPadding="0" border="0">
      <tbody style="width:100%">
      <tr style="width:100%">
        <td style="width:22px;font-weight:bold;vertical-align:bottom">
          <#if !isSuccessful>
              <@icons.failure serverUrl=serverUrl />
          <#else>
              <@icons.success serverUrl=serverUrl />
          </#if>
        </td>
        <td>
          <a href='${jobUrl}' style="font-size:14px;line-height:14px;margin:0;color:#0F5B99;vertical-align:bottom;text-decoration: none;">${name}
            <#if time??><span style="color:#737577">(${time})</span></#if>
          </a>
        </td>
      </tr>
      </tbody>
    </table>
  </#if>
</#macro>

<#macro reportLine text="" title=false>
  <p style="font-size:12px;line-height:<#if title>24<#else>20</#if>px;margin:2px 0;<#if title>font-weight:bold;</#if>color:#1F2326">
    ${text}
  </p>
</#macro>

<#macro job pipeline serverUrl job=null isSuccessful=false >
  <@jobTitle serverUrl=serverUrl jobUrl=job.url name=job.name time=job.duration isSuccessful=isSuccessful/>
    <#if !isSuccessful>
      <@section.gap size=12 />
      <@section.code>
        <#if (job.problems?size>0)>
          <@reportLine text="Build Problems" title=true />
          <#list job.problems as problem>
            <#if problem.description?has_content>
              <#assign text="${problem?counter}. ${problem.description?html}" />
              <@reportLine text />
            </#if>
          </#list>
        </#if>
        <#if job.failedTests?? && (job.failedTests.testsForSummary?size>0)>
          <@section.gap size=8 />
          <@reportLine text="Tests failed" title=true />
          <#list job.failedTests.testsForSummary as item>
            <#assign text="${item?counter}. ${item.test.name}" />
            <@reportLine text />
          </#list>
        </#if>
      </@section.code>
    </#if>
    <#if isSuccessful>
        <@section.gap size=12 />
      <#else>
        <@section.gap size=20 />
    </#if>
</#macro>
