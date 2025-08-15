<#import "pipeline.icons.ftl" as icons />
<#import "pipeline.content.ftl" as content />
<#import "pipeline.footer.ftl" as footer />

<#macro body serverUrl pipeline showLogo="true">
  <body style="background-color:#F7F9FA;font-family:-apple-system,BlinkMacSystemFont,&quot;Segoe UI&quot;,Roboto,Oxygen-Sans,Ubuntu,Cantarell,&quot;Helvetica Neue&quot;,sans-serif">
  <table align="center" width="100%" border="0" cellPadding="0" cellSpacing="0" role="presentation">
    <tbody>
      <tr style="width:100%">
        <td>
            <#if showLogo == "true"><@icons.headerLogo serverUrl /></#if>
            <@content.content pipeline=pipeline>
            <#nested />
          </@content.content>
        </td>
      </tr>
    </tbody>
  </table>
  </body>
</#macro>
