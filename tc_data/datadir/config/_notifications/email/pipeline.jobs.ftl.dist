<#import "common.ftl" as common>
<#import "pipeline.typography.ftl" as typography>
<#import "pipeline.job.ftl" as job>
<#import "pipeline.section.ftl" as section>

<#macro jobs serverUrl isSuccessful=false pipeline=null>
    <#if pipeline?has_content>
    <#if isSuccessful>
        <#assign jobsNumber=pipeline.successfulJobs?size />
        <@section.gap />
        <@typography.text text="${jobsNumber} success jobs" bold=true />
        <@section.gap size=16 />
        <#list pipeline.successfulJobs as jobItem>
            <@job.job pipeline=pipeline serverUrl=serverUrl job=jobItem isSuccessful=true/>
        </#list>
    <#else>
        <#assign jobsNumber=pipeline.failedJobs?size />
        <@section.gap />
        <@typography.text text="${jobsNumber} failed jobs" bold=true />
        <@section.gap size=16 />
        <#list pipeline.failedJobs as jobItem>
            <@job.job pipeline=pipeline serverUrl=serverUrl job=jobItem />
        </#list>
    </#if>
    </#if>
</#macro>
