<#import "pipeline.button.ftl" as button />
<#import "pipeline.typography.ftl" as typography />

<#macro intro pipeline build timestamp="" userName="">
    <#if pipeline??>
      <table align="center" width="100%" data-tc-attr="meta-row" style="margin-bottom:0px;text-align:left;height:24px" role="presentation" cellSpacing="0" cellPadding="0" border="0">
        <tbody style="width:100%">
        <tr style="width:100%">
          <td data-tc-attr="meta-key" style="min-width:80px;width:80px;font-weight:bold;height:24px">
              <@typography.text text="Pipeline" bold=true />
          </td>
          <td data-tc-attr="meta-value" style="text-align:left;padding-left:8px;height:24px">
              <@typography.text text="${pipeline.name}" href="${pipeline.pipelineRunUrl}" />
          </td>
        </tr>
        </tbody>
      </table>
    </#if>
    <#if build.branch??>
      <table align="center" width="100%" data-tc-attr="meta-row" style="margin-bottom:0px;text-align:left;height:24px" role="presentation" cellSpacing="0" cellPadding="0" border="0">
        <tbody style="width:100%">
        <tr style="width:100%">
          <td data-tc-attr="meta-key"  style="min-width:80px;width:80px;font-weight:bold;height:24px">
              <@typography.text text="Branch" bold=true />
          </td>
          <td data-tc-attr="meta-value" style="text-align:left;padding-left:8px;height:24px">
              <@typography.text text="${build.branch.displayName?html}" />
          </td>
        </tr>
        </tbody>
      </table>
    </#if>
    <#if timestamp?has_content && userName?has_content>
      <table align="center" width="100%" data-tc-attr="meta-row" style="margin-bottom:0px;text-align:left;height:24px" role="presentation" cellSpacing="0" cellPadding="0" border="0">
        <tbody style="width:100%">
        <tr style="width:100%">
          <td data-tc-attr="meta-key"  style="min-width:80px;width:80px;font-weight:bold;height:24px">
              <@typography.text text="Triggered" bold=true />
          </td>
          <td data-tc-attr="meta-value" style="text-align:left;padding-left:8px;height:24px">
              <@typography.text text="${timestamp}; by ${userName}" />
          </td>
        </tr>
        </tbody>
      </table>
    </#if>
    <#if pipeline?? && pipeline.pipelineRunUrl??>
      <table align="center" width="100%" role="presentation" cellSpacing="0" cellPadding="0" border="0">
        <tbody style="width:100%">
        <tr style="width:100%">
            <@button.button text="Open Run" url="${pipeline.pipelineRunUrl}" />
        </tr>
        </tbody>
      </table>
    </#if>
</#macro>
