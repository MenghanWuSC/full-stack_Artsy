import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';

import { CommunicateService } from '../communicate.service';
// Interface: artist card
import { Artist } from '../artist';
import { Details } from '../details';
import { Artwork } from '../artwork';
import { Gene } from '../gene';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    private communicateService: CommunicateService
  ) { }

  // Properties (public)
  searchForm = this.formBuilder.group({
    query: new FormControl('', Validators.required)
  });
  artists:Artist[] = [];
  details:Details = {name: '', birthday: '', deathday: '', nationality: '', biography: ''};
  artworks:Artwork[] = [];
  clicked_artwork:Artwork = {artwork_ID: '', artwork_title: '', artwork_date: '', artwork_thumbnail: ''};
  genes:Gene[] = [];
  null_artist:boolean = false;
  null_artwork:boolean = false;
  null_gene:boolean = false;
  // Condition properties (static)
  click_submit:boolean = false;
  click_detail:boolean = false;
  click_gene:boolean = false;

  onClear(): void {
    console.warn("Cleanup");
    // Cleanup
    this.artists = [];
    this.details = {name: '', birthday: '', deathday: '', nationality: '', biography: ''};
    this.artworks = [];
    this.clicked_artwork = {artwork_ID: '', artwork_title: '', artwork_date: '', artwork_thumbnail: ''};
    this.genes = [];
    this.null_artist = false;
    this.null_artwork = false;
    this.null_gene = false;
  }

  onSubmit(): void {
    console.warn("query=", this.searchForm.controls['query'].value);
    if (this.searchForm.controls['query'].value == "" || this.searchForm.controls['query'].value == null) {
      // Empty query then abort
      return;
    }
    // Preface: condition & cleanup & local variables
    this.click_submit = true;
    this.onClear();
    let artist:Artist = {title: '', self: '', thumbnail: ''};
    let src_img:string = "";
    // Call service
    // *TO-DO: make sure NONE null...
    this.communicateService.searchArtist(this.searchForm.controls['query'].value!)
    .subscribe(res => {
      for(let item of res){
        if (item.type != 'artist') {
          continue;
        }
        console.log(item.title, item._links.self.href.substring(item._links.self.href.lastIndexOf('/') + 1), item._links.thumbnail.href);
        if (item._links.thumbnail.href == "/assets/shared/missing_image.png") {
          src_img = "assets/images/artsy_logo.svg";
        }
        else {
          src_img = item._links.thumbnail.href;
        }
        artist = {
          title: item.title,
          self: item._links.self.href.substring(item._links.self.href.lastIndexOf('/') + 1),
          thumbnail: src_img
        };
        this.artists.push(artist);
      }
      // Callback finished
      if (this.artists.length == 0) {
        this.null_artist = true;
      }
      this.click_submit = false;
    });
  }

  onDetail(person: Artist): void {
    console.warn("Clicked:", person.title, person.self);
    // Preface: condition & cleanup & local variables
    this.click_detail = true;
    this.details = {name: '', birthday: '', deathday: '', nationality: '', biography: ''};
    this.artworks = [];
    this.clicked_artwork = {artwork_ID: '', artwork_title: '', artwork_date: '', artwork_thumbnail: ''};
    this.genes = [];
    this.null_artwork = false;
    let artwork:Artwork = {artwork_ID: '', artwork_title: '', artwork_date: '', artwork_thumbnail: ''};
    let src_img:string = "";
    // Call services
    // 1. Details
    this.communicateService.artistDetail(person.self)
    .subscribe(res => {
      this.details = {
        name: res.name,
        birthday: res.birthday,
        deathday: res.deathday,
        nationality: res.nationality,
        biography: res.biography
      };
      // Callback finished
      this.click_detail = false;
    });
    // 2. Artworks
    this.communicateService.artistArtwork(person.self)
    .subscribe(res => {
      for(let item of res){
        if (item._links.thumbnail.href == "/assets/shared/missing_image.png") {
          src_img = "assets/images/artsy_logo.svg";
        }
        else {
          src_img = item._links.thumbnail.href;
        }
        artwork = {
          artwork_ID: item.id,
          artwork_title: item.title,
          artwork_date: item.date,
          artwork_thumbnail: src_img
        };
        this.artworks.push(artwork);
      }
      // Callback finished
      if (this.artworks.length == 0) {
        this.null_artwork = true;
      }
    });
  }

  onGene(product:Artwork): void {
    console.warn("Clicked:", product.artwork_title, product.artwork_ID);
    // Preface: cleanup & local variables
    this.click_gene = true;
    this.clicked_artwork = {artwork_ID: '', artwork_title: '', artwork_date: '', artwork_thumbnail: ''};
    this.genes = [];
    this.null_gene = false;
    let gene:Gene = {gene_name: '', geng_thumbnail: ''};
    let src_img:string = "";
    // Save the clicked product/artwork for 'Modal'
    this.clicked_artwork = {
      artwork_ID: product.artwork_ID,
      artwork_title: product.artwork_title,
      artwork_date: product.artwork_date,
      artwork_thumbnail: product.artwork_thumbnail
    }
    // Call service
    this.communicateService.artworkGene(product.artwork_ID)
    .subscribe(res => {
      for(let item of res) {
        if (item._links.thumbnail.href == "/assets/shared/missing_image.png") {
          src_img = "assets/images/artsy_logo.svg";
        }
        else {
          src_img = item._links.thumbnail.href;
        }
        gene = {
          gene_name: item.name,
          geng_thumbnail: src_img
        }
        this.genes.push(gene);
      }
      // Callback finished
      if (this.genes.length == 0) {
        this.null_gene = true;
      }
      this.click_gene = false;
    });
  }

  ngOnInit(): void {
  }

}
