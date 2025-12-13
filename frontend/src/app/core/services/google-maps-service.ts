import {Injectable} from '@angular/core';
import { environment } from '@env/environment';

@Injectable ({
  providedIn: 'root'
})
export class GoogleMapsService {
  private apiKey = environment.googleMapsApiKey;
  private caricato = false;

  load():Promise<void>{
    return new Promise((resolve,reject)=>{
      if(this.caricato || (window as any).google?.maps){
        resolve();
        return;
      }
      if(document.getElementById("google-maps-script")){
        resolve();
        this.caricato = true;
        return;
      }
      const script = document.createElement('script');
      script.id = "google-maps-script";
      script.src = `https://maps.googleapis.com/maps/api/js?key=${this.apiKey}&libraries=places`;
      script.async = true;
      script.defer = true;
      script.onload = () => {
        this.caricato = true;
        resolve();
      }
      script.onerror = (error) => reject(error);
      document.body.appendChild(script);
    })
  };
}
