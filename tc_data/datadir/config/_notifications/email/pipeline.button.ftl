<#macro button text=null url=null>
  <a data-tc-attr="button" href="${url!"#"}" target="_blank" style="background-color:#0080E5;border-radius:4px;color:#fff;font-size:14px;text-decoration:none;text-align:center;display:inline-block;width:auto;margin-top:14px;font-weight:400;line-height:100%;max-width:100%;padding:6px 20px">
    <span>
      <!--[if mso]><i style="letter-spacing: 20px;mso-font-width:-100%;mso-text-raise:9" hidden>&nbsp;</i><![endif]-->
    </span>
    <span style="max-width:100%;display:inline-block;line-height:120%;mso-padding-alt:0px;mso-text-raise:4.5px">${text!"Open Run"}</span>
    <span>
      <!--[if mso]><i style="letter-spacing: 20px;mso-font-width:-100%" hidden>&nbsp;</i><![endif]-->
    </span>
  </a>
</#macro>
