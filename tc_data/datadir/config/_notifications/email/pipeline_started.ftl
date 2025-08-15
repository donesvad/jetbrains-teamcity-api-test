

<#-- Uses FreeMarker template syntax, template guide can be found at http://freemarker.org/docs/dgui.html -->

<#-- @ftlvariable name="build" type="jetbrains.buildServer.serverSide.SBuild" -->
<#-- @ftlvariable name="serverUrl" type="java.lang.String" -->
<#-- @ftlvariable name="timestamp" type="java.lang.String" -->
<#-- @ftlvariable name="userName" type="java.lang.String" -->
<#-- @ftlvariable name="pipelineEditUrl" type="java.lang.String" -->

<#global tableBackground = "#EDF3FF">
<#global textColor = "#3574F0">
<#global titleText = "Pipeline started">

<#import "common.ftl" as common>
<#import "responsibility.ftl" as resp>
<#import "pipeline.body.ftl" as body>
<#import "pipeline.intro.ftl" as intro>

<#global subject>[TeamCity Pipelines, STARTED] Pipeline ${pipeline.name} <@common.short_build_info build/></#global>

<#global body>Pipeline ${pipeline.name} <@common.short_build_info build/> started ${var.buildShortStatusDescription}.
    <@common.footer/>
</#global>

<#global bodyHtml>
    <@body.body serverUrl=serverUrl pipeline=pipeline>
        <@intro.intro pipeline=pipeline build=build timestamp=timestamp userName=userName/>
    </@body.body>
</#global>
