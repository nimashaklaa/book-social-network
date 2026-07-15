import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root', // ← this is the HTML tag name
  imports: [RouterOutlet],
  templateUrl: './app.html', // ← its HTML
  styleUrl: './app.scss'
})
//It's metadata that tells Angular "this class is a component"
export class App {
  protected readonly title = signal('book-network-ui');
}
