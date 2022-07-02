import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommunicateService {

  constructor(private http: HttpClient) { }
  
  // Get artists by given query term 'GET'
  searchArtist(term: string): Observable<any> {
    const url = "api/search";
    // Add safe, URL encoded search parameter if there is a search term
    term = term.trim();
    const options = term ?
      { params: new HttpParams().set('query', term) } : {};
    // Process 'GET'
    return this.http.get<any>(url, options);
  }

  // Get given artist's details 'GET'
  artistDetail(id: string): Observable<any> {
    const url = "api/detail";
    const options = id ?
      { params: new HttpParams().set('id', id) } : {};
    // Process 'GET'
    return this.http.get<any>(url, options);
  }

  // Get given artist's artworks 'GET'
  artistArtwork(id: string): Observable<any> {
    const url = "api/artwork";
    const options = id ?
      { params: new HttpParams().set('id', id) } : {};
    // Process 'GET'
    return this.http.get<any>(url, options);
  }

  // Get given artwork's genes 'GET'
  artworkGene(id: string): Observable<any> {
    const url = "api/gene";
    const options = id ?
      { params: new HttpParams().set('id', id) } : {};
    // Process 'GET'
    return this.http.get<any>(url, options);
  }

}
