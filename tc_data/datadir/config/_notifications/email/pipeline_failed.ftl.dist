<#-- Uses FreeMarker template syntax, template guide can be found at http://freemarker.org/docs/dgui.html -->

<#-- @ftlvariable name="build" type="jetbrains.buildServer.serverSide.SBuild" -->
<#-- @ftlvariable name="serverUrl" type="java.lang.String" -->
<#global tableBackground = "#FAD4D8">
<#global textColor = "#CC3645">
<#global titleText = "Pipeline failed">

<#import "common.ftl" as common>
<#import "responsibility.ftl" as resp>
<#import "pipeline.body.ftl" as body>
<#import "pipeline.intro.ftl" as intro>
<#import "pipeline.jobs.ftl" as jobs>
<#import "pipeline.changes.ftl" as changes>

<#global subject>[TeamCity Pipelines, FAILED] Pipeline ${pipeline.name} <@common.short_build_info build/></#global>

<#global body>Pipeline ${pipeline.name} <@common.short_build_info build/> failed ${var.buildShortStatusDescription}.
    <@resp.buildTypeInvestigation buildType false/>
    <#if !build.agentLessBuild>Agent: ${agentName}</#if>
  Build results: ${link.buildResultsLink}

    ${var.buildCompilationErrors}${var.buildFailedTestsErrors}${var.buildChanges}
    <@common.footer/>
</#global>

<#global bodyHtml>
    <@body.body serverUrl=serverUrl pipeline=pipeline>
        <@intro.intro pipeline build/>
        <@jobs.jobs serverUrl=serverUrl pipeline=pipeline  />
        <@changes.changes bean=var.changesBean link=pipeline.changesListUrl />
    </@body.body>
</#global>
