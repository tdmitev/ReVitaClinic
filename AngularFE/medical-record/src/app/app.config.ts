import { APP_INITIALIZER, importProvidersFrom } from '@angular/core';
import { provideRouter }             from '@angular/router';
import { HttpClientModule }          from '@angular/common/http';
import {
  KeycloakAngularModule,
  KeycloakService,
  KeycloakOptions
} from 'keycloak-angular';
import { routes } from './app.routes';

function initializeKeycloak(keycloak: KeycloakService) {
  const kcConfig = {
    url:         'http://localhost:8080',  
    realm:       'ReVitaClinic',
    clientId:    'ReVitaClinic',
    credentials: {
      secret: 'qavWqr1mW5DXgfg2mv1ZNm53Th7V8KR4'
    }
  } as any;

  const options: KeycloakOptions = {
    config: kcConfig,
    initOptions: {
      onLoad:           'login-required',
      pkceMethod:       'S256',
      checkLoginIframe: false,
      redirectUri:      window.location.origin
    }
  };

  return () =>
    keycloak.init(options)
      .then(ok  => console.debug('KC init OK', ok))
      .catch(err => console.error('KC init FAILED', err));
}

export const appConfig = {
  providers: [
    provideRouter(routes),
    importProvidersFrom(HttpClientModule, KeycloakAngularModule),
    {
      provide:    APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi:      true,
      deps:       [KeycloakService]
    }
  ]
};