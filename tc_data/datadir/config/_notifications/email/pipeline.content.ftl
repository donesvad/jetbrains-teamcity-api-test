<#macro title text>
  <#if text??>
    <table align="center" width="100%" style="background:${tableBackground};
        border-radius:8px 8px 0 0;text-align:center" border="0" cellPadding="0" cellSpacing="0" role="presentation">
      <tbody>
      <tr>
        <td>
          <p style="font-size:12px;line-height:1.5;margin:16px 0 14px;font-weight:600;color:${textColor};text-transform:uppercase">${text}</p>
        </td>
      </tr>
      </tbody>
    </table>
  </#if>
</#macro>

<#macro footer pipeline>
  <#if pipeline??>
    <table align="center" width="100%" style="background:#F7F8FA;
        border-radius:8px 8px 0 0;text-align:center" border="0" cellPadding="0" cellSpacing="0" role="presentation">
      <tbody>
      <tr>
        <td>
          <p style="font-size:12px;margin:16px 0 14px;">
            You are receiving this because you are subscribed to this pipeline.<br>
            <a href="${pipeline.pipelineUrl}">Unsubscribe</a> from this pipeline notifications.
          </p>
        </td>
      </tr>
      </tbody>
    </table>
    </#if>
</#macro>

<#macro content pipeline>
  <table align="center" width="100%" data-tc-attr="layout-root" role="presentation" cellSpacing="0" cellPadding="0" border="0" style="max-width:800px;margin:0 auto;padding:32px 0.5em 48px;width:100%">
    <tbody>
    <tr>
      <td>
          <@title text=titleText />
        <table align="center" width="100%" style="border-radius:0 0 8px 8px;max-width:800px;background:white" border="0" cellPadding="0" cellSpacing="0" role="presentation">
          <tbody>
          <tr>
            <td>
              <table align="center" width="100%" id="layout-container" role="presentation" cellSpacing="0" cellPadding="0" border="0" style="max-width:650px;padding:32px 20px">
                <tbody>
                  <tr style="width:100%">
                    <td>
                      <#nested />
                    </td>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
          </tbody>
        </table>
          <@footer pipeline=pipeline/>
      </td>
    </tr>
    </tbody>
  </table>
</#macro>
